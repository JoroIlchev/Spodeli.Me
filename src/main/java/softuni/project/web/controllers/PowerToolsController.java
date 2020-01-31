package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.interfaces.PowerToolsService;
import softuni.project.services.models.PowerToolsServiceModel;
import softuni.project.validations.offers.PowerToolsAddValidation;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.PowerToolsAddBindingModel;
import softuni.project.web.models.view.PowerToolsViewModel;
import softuni.project.web.models.view.edit.PowerToolEditViewModel;

import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;

import static softuni.project.web.ControllersConstants.*;

@Controller
@RequestMapping("/offer")
public class PowerToolsController extends BaseController {

    private final PowerToolsService powerToolsService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final PowerToolsAddValidation powerToolsAddValidation;

    @Autowired
    public PowerToolsController(PowerToolsService powerToolsService, CloudinaryService cloudinaryService, ModelMapper modelMapper, PowerToolsAddValidation powerToolsAddValidation) {
        this.powerToolsService = powerToolsService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.powerToolsAddValidation = powerToolsAddValidation;
    }

    @GetMapping("/add/powertool")
    @PreAuthorize(("isAuthenticated()"))
    @PageTitle("Add Power Tools")
    public ModelAndView addPowerTools(ModelAndView modelAndView, @ModelAttribute(name = "model")
            PowerToolsAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("offers/powerTools/add-powertools", modelAndView);
    }

    @PostMapping("/add/powertool")
    @PreAuthorize(("isAuthenticated()"))
    public ModelAndView addPowerToolsConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model")
            PowerToolsAddBindingModel model, Principal principal, BindingResult bindingResult) throws IOException {

        powerToolsAddValidation.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("offers/powerTools/add-powertools", modelAndView);
        }
        PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(model, PowerToolsServiceModel.class);
        if (model.getImage().isEmpty()) {
            powerToolsServiceModel.setImage(NO_IMAGE);
        } else {
            powerToolsServiceModel.setImage(cloudinaryService.uploadImg(model.getImage()));
        }
        String username = getUsername(principal);
        powerToolsService.savePowerTools(powerToolsServiceModel, username);
        return redirect("/home");
    }

    @GetMapping("/powertools/details/{id}")
    @PageTitle("Offer Details")
    public ModelAndView powerToolDetails(@PathVariable String id, ModelAndView modelAndView) {
        PowerToolsServiceModel powerToolsServiceModel = powerToolsService.findById(id);
        PowerToolsViewModel powerToolsViewModel = modelMapper.map(powerToolsServiceModel, PowerToolsViewModel.class);
        powerToolsViewModel.setRegion(powerToolsServiceModel.getRegion().getName());
        setBooleans(powerToolsServiceModel, powerToolsViewModel);
        modelAndView.addObject("model", powerToolsViewModel);
        return view("offers/powertools/powertool-details", modelAndView);
    }

    @GetMapping("/powertools/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Offer Edit")
    public ModelAndView powerToolEdit(@PathVariable String id, ModelAndView modelAndView) {
        PowerToolsServiceModel powerToolsServiceModel = powerToolsService.findById(id);
        PowerToolEditViewModel powerToolEditViewModel = modelMapper.map(powerToolsServiceModel, PowerToolEditViewModel.class);
        powerToolEditViewModel.setCategoryUrl(powerToolsServiceModel.getCategoryServiceModel().getCategoryUrl());

        modelAndView.addObject("model", powerToolEditViewModel);
        return view("offers/powertools/powertool-edit", modelAndView);
    }

    @PostMapping("/powertools/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView powerToolEditConfirm(@PathVariable String id, ModelAndView modelAndView,
                                             @ModelAttribute(name = "model") PowerToolsAddBindingModel model, BindingResult bindingResult) throws IOException {
        powerToolsAddValidation.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            model.setId(id);
            modelAndView.addObject("model", model);
            return view("offers/powertools/powertool-edit", modelAndView);
        }
        PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(model, PowerToolsServiceModel.class);
        if (!model.getImage().isEmpty()) {
            powerToolsServiceModel.setImage(cloudinaryService.uploadImg(model.getImage()));
        } else {
            powerToolsServiceModel.setImage(NO_IMAGE);
        }
        powerToolsService.editOffer(powerToolsServiceModel, id);

        return redirect("/offer/my/all");
    }

    @GetMapping("/powertools/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView clothDelete(@PathVariable String id) {
        powerToolsService.deleteById(id);

        return redirect("/offer/my/all");
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private void setBooleans(PowerToolsServiceModel powerToolsServiceModel, PowerToolsViewModel powerToolsViewModel) {
        if (powerToolsServiceModel.getIsPortable()) {
            powerToolsViewModel.setIsPortable(BOOL_TO_STRING_YES);
        } else {
            powerToolsViewModel.setIsPortable(BOOL_TO_STRING_NO);
        }
        if (powerToolsServiceModel.getIsNeedExtraEquipment()) {
            powerToolsViewModel.setIsNeedExtraEquipment(BOOL_TO_STRING_YES);
        } else {
            powerToolsViewModel.setIsNeedExtraEquipment(BOOL_TO_STRING_NO);
        }
        if (powerToolsServiceModel.getIsNeedSpecialSkills()) {
            powerToolsViewModel.setIsNeedSpecialSkills(BOOL_TO_STRING_YES);
        } else {
            powerToolsViewModel.setIsNeedSpecialSkills(BOOL_TO_STRING_NO);
        }
        if (powerToolsServiceModel.getDepositNeeded()) {
            powerToolsViewModel.setDepositNeeded(BOOL_TO_STRING_YES);
        } else {
            powerToolsViewModel.setDepositNeeded(BOOL_TO_STRING_NO);
        }
        powerToolsViewModel.setStartsOn(powerToolsServiceModel.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        powerToolsViewModel.setEndsOn(powerToolsServiceModel.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

    }
}
