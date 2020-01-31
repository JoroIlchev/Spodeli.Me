package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.interfaces.VehicleService;
import softuni.project.services.models.VehicleServiceModel;
import softuni.project.validations.offers.VehicleAddValidation;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.VehicleAddBindingModel;
import softuni.project.web.models.view.VehicleViewModel;
import softuni.project.web.models.view.edit.VehiclesEditViewModel;

import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;

import static softuni.project.web.ControllersConstants.*;

@Controller
@RequestMapping("/offer")
public class VehiclesController extends BaseController {

    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final VehicleService vehicleService;
    private final VehicleAddValidation vehicleValidator;

    @Autowired
    public VehiclesController(ModelMapper modelMapper, CloudinaryService cloudinaryService, VehicleService vehicleService, VehicleAddValidation vehicleValidator) {
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.vehicleService = vehicleService;
        this.vehicleValidator = vehicleValidator;
    }

    @GetMapping("/add/vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Offer")
    public ModelAndView addVehicle(ModelAndView modelAndView, @ModelAttribute VehicleAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("offers/vehicles/add-vehicles", modelAndView);
    }

    @PostMapping("/add/vehicle")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addVehicleConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") VehicleAddBindingModel model,
                                          Principal principal, BindingResult bindingResult) throws IOException {
        vehicleValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("offers/vehicles/add-vehicle", modelAndView);
        }
        String username = getUsername(principal);
        VehicleServiceModel vehicle = modelMapper.map(model, VehicleServiceModel.class);
        imgValidate(model, vehicle);
        vehicleService.saveVehicle(vehicle, username);
        return redirect("/home");
    }



    @GetMapping("/vehicle/details/{id}")
    @PageTitle("Offer Details")
    public ModelAndView tripDetails(@PathVariable String id, ModelAndView modelAndView) {
        VehicleServiceModel vehicleServiceModel = vehicleService.findById(id);
        VehicleViewModel vehicleViewModel = modelMapper.map(vehicleServiceModel, VehicleViewModel.class);
        setViewModel(vehicleServiceModel, vehicleViewModel);
        modelAndView.addObject("model", vehicleViewModel);
        return view("offers/vehicles/vehicle-details", modelAndView);
    }

    @GetMapping("/vehicles/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Offer Edit")
    public ModelAndView vehicleEdit(@PathVariable String id, ModelAndView modelAndView) {
        VehicleServiceModel vehicleServiceModel = vehicleService.findById(id);
        VehiclesEditViewModel vehiclesEditViewModel = modelMapper.map(vehicleServiceModel, VehiclesEditViewModel.class);
        vehiclesEditViewModel.setCategoryUrl(vehicleServiceModel.getCategoryServiceModel().getCategoryUrl());

        modelAndView.addObject("model", vehiclesEditViewModel);
        return view("offers/vehicles/vehicle-edit", modelAndView);
    }

    @PostMapping("/vehicles/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView powerToolEditConfirm(@PathVariable String id, ModelAndView modelAndView,
                                             @ModelAttribute (name = "model") VehicleAddBindingModel model, BindingResult bindingResult) throws IOException {
         vehicleValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            model.setId(id);
            modelAndView.addObject("model", model);
            return view("offers/vehicles/vehicle-edit", modelAndView);
        }
        VehicleServiceModel vehicleServiceModel = modelMapper.map(model, VehicleServiceModel.class);
        imgValidate(model, vehicleServiceModel);
        vehicleService.editOffer(vehicleServiceModel, id);

        return redirect("/offer/my/all");
    }

    @GetMapping("/vehicles/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView clothDelete(@PathVariable String id) {
        vehicleService.deleteById(id);

        return redirect("/offer/my/all");
    }


    private void setViewModel(VehicleServiceModel vehicleServiceModel, VehicleViewModel vehicleViewModel) {
        vehicleViewModel.setRegion(vehicleServiceModel.getRegion().getName());
        if (vehicleServiceModel.getDrivingLicenseNeeded()) {
            vehicleViewModel.setDrivingLicenseNeeded(BOOL_TO_STRING_YES);
        } else {
            vehicleViewModel.setDrivingLicenseNeeded(BOOL_TO_STRING_NO);
        }
        vehicleViewModel.setGearBox(vehicleServiceModel.getGearBox().getName());
        vehicleViewModel.setStartsOn(vehicleServiceModel.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        vehicleViewModel.setEndsOn(vehicleServiceModel.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

    }
    private void imgValidate(@ModelAttribute(name = "model") VehicleAddBindingModel model, VehicleServiceModel vehicle) throws IOException {
        if (!model.getImage().isEmpty()) {
            vehicle.setImage(cloudinaryService.uploadImg(model.getImage()));
        } else {
            vehicle.setImage(NO_IMAGE);
        }
    }
}
