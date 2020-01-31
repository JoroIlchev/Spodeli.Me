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
import softuni.project.data.entities.enums.Extras;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.interfaces.RealEstateService;
import softuni.project.services.models.RealEstateServiceModel;
import softuni.project.validations.offers.RealEstateAddValidation;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.RealEstateAddBindingModel;
import softuni.project.web.models.view.RealEstateViewModel;
import softuni.project.web.models.view.edit.RealEstateEditViewModel;

import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static softuni.project.web.ControllersConstants.*;

@Controller
@RequestMapping("/offer")
public class RealEstateController extends BaseController {

    private final ModelMapper modelMapper;
    private final RealEstateService realEstateService;
    private final CloudinaryService cloudinaryService;
    private final RealEstateAddValidation realEstateAddValidation;

    @Autowired
    public RealEstateController(ModelMapper modelMapper, RealEstateService realEstateService, CloudinaryService cloudinaryService, RealEstateAddValidation realEstateAddValidation) {
        this.modelMapper = modelMapper;
        this.realEstateService = realEstateService;
        this.cloudinaryService = cloudinaryService;
        this.realEstateAddValidation = realEstateAddValidation;
    }

    @GetMapping("/add/realestate")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Offer")
    public ModelAndView addRealEstate(ModelAndView modelAndView, @ModelAttribute RealEstateAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("offers/realestates/add-realEstate", modelAndView);
    }

    @PostMapping("/add/realestate")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addRealEstateConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") RealEstateAddBindingModel model,
                                             BindingResult bindingResult, Principal principal) throws IOException {
        realEstateAddValidation.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("offers/realestates/add-realEstate", modelAndView);
        }
        String username = getUsername(principal);
        RealEstateServiceModel realEstateServiceModel = modelMapper.map(model, RealEstateServiceModel.class);

        imgValidate(model, realEstateServiceModel);
        realEstateService.saveRealEstate(realEstateServiceModel, username);
        return redirect("/home");
    }



    @GetMapping("/realestates/details/{id}")
    @PageTitle("Offer Details")
    public ModelAndView realEstateDetails(@PathVariable String id, ModelAndView modelAndView) {
        RealEstateServiceModel realEstateServiceModel = realEstateService.findById(id);
        RealEstateViewModel realEstateViewModel = modelMapper.map(realEstateServiceModel, RealEstateViewModel.class);
        realEstateViewModel.setRegion(realEstateServiceModel.getRegion().getName());
        realEstateViewModelSetter(realEstateServiceModel, realEstateViewModel);
        modelAndView.addObject("model", realEstateViewModel);
        return view("offers/realestates/realestate-details", modelAndView);
    }

    @GetMapping("/realestates/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Offer Edit")
    public ModelAndView realEstateEdit(@PathVariable String id, ModelAndView modelAndView) {
        RealEstateServiceModel realEstateServiceModel = realEstateService.findById(id);
        RealEstateEditViewModel realEstateEditViewModel = modelMapper.map(realEstateServiceModel, RealEstateEditViewModel.class);
        realEstateEditViewModel.setExtras(realEstateServiceModel.getExtras());
        realEstateEditViewModel.setCategoryUrl(realEstateServiceModel.getCategoryServiceModel().getCategoryUrl());

        modelAndView.addObject("model", realEstateEditViewModel);
        return view("offers/realestates/realestate-edit", modelAndView);
    }

    @PostMapping("/realestates/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView powerToolEditConfirm(@PathVariable String id, ModelAndView modelAndView,
                                             @ModelAttribute(name = "model") RealEstateAddBindingModel model, BindingResult bindingResult) throws IOException {

        realEstateAddValidation.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            model.setId(id);
            modelAndView.addObject("model", model);
            return view("offers/realestates/realestate-edit", modelAndView);
        }
        RealEstateServiceModel realEstateServiceModel = modelMapper.map(model, RealEstateServiceModel.class);
        imgValidate(model, realEstateServiceModel);
        realEstateService.editOffer(realEstateServiceModel, id);
        return redirect("/offer/my/all");
    }

    @GetMapping("/realestates/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView clothDelete(@PathVariable String id) {
        realEstateService.deleteById(id);
        return redirect("/offer/my/all");
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    private void realEstateViewModelSetter(RealEstateServiceModel realEstateServiceModel, RealEstateViewModel realEstateViewModel) {
        if (realEstateServiceModel.getIsPartyFree()) {
            realEstateViewModel.setIsPartyFree(BOOL_TO_STRING_YES);
        } else {
            realEstateViewModel.setIsPartyFree(BOOL_TO_STRING_NO);
        }
        List<String> extras = new ArrayList<>();
        for (Extras extra : realEstateServiceModel.getExtras()) {
            extras.add(extra.name());
        }
        realEstateViewModel.setExtras(String.join(", ", extras));
        realEstateViewModel.setType(realEstateServiceModel.getType().name());
        realEstateViewModel.setStartsOn(realEstateServiceModel.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        realEstateViewModel.setEndsOn(realEstateServiceModel.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

    }

    private void imgValidate(@ModelAttribute(name = "model") RealEstateAddBindingModel model, RealEstateServiceModel realEstateServiceModel) throws IOException {
        if (!model.getImage().isEmpty()) {
            realEstateServiceModel.setImage(cloudinaryService.uploadImg(model.getImage()));
        } else {
            realEstateServiceModel.setImage(NO_IMAGE);
        }
    }
}
