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
import softuni.project.services.interfaces.ClothesService;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.validations.offers.ClothesAddValidation;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.ClothesAddBindingModel;
import softuni.project.web.models.view.ClothesViewModel;
import softuni.project.web.models.view.edit.ClothEditViewModel;

import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;

import static softuni.project.web.ControllersConstants.*;

@Controller
@RequestMapping("/offer")
public class ClothesController extends BaseController {

    private final ModelMapper modelMapper;
    private final ClothesService clothesService;
    private final CloudinaryService cloudinaryService;
    private final ClothesAddValidation clothesAddValidation;

    @Autowired
    public ClothesController(ModelMapper modelMapper, ClothesService clothesService, CloudinaryService cloudinaryService, ClothesAddValidation clothesAddValidation) {
        this.modelMapper = modelMapper;
        this.clothesService = clothesService;
        this.cloudinaryService = cloudinaryService;
        this.clothesAddValidation = clothesAddValidation;
    }

    @GetMapping("/add/clothes")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Clothes")
    public ModelAndView addClothes(ModelAndView modelAndView, @ModelAttribute ClothesAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("offers/clothes/add-clothes", modelAndView);
    }

    @PostMapping("/add/clothes")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addClothesConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") ClothesAddBindingModel model,
                                          Principal principal, BindingResult bindingResult) throws IOException {

        clothesAddValidation.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("offers/clothes/add-clothes", modelAndView);
        }
        String username = getUsername(principal);
        ClothesServiceModel clothesServiceModel = modelMapper.map(model, ClothesServiceModel.class);
        if (model.getImage().isEmpty()) {
            clothesServiceModel.setImage(NO_IMAGE);
        }else {
            clothesServiceModel.setImage(cloudinaryService.uploadImg(model.getImage()));
        }
        clothesService.saveClothes(clothesServiceModel, username);
        return redirect("/home");
    }

    @GetMapping("/clothes/details/{id}")
    @PageTitle("Offer Details")
    public ModelAndView clothDetails(@PathVariable String id, ModelAndView modelAndView) {
        ClothesServiceModel clothesServiceModel = clothesService.findById(id);
        ClothesViewModel model = modelMapper.map(clothesServiceModel, ClothesViewModel.class);
        clothesViewSetter(clothesServiceModel, model);
        modelAndView.addObject("model", model);
        return view("offers/clothes/clothes-details", modelAndView);

    }

    @GetMapping("/clothes/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Offer Edit")
    public ModelAndView clothEdit(@PathVariable String id, ModelAndView modelAndView) {
        ClothesServiceModel clothesServiceModel = clothesService.findById(id);
        ClothEditViewModel clothEditViewModel = modelMapper.map(clothesServiceModel, ClothEditViewModel.class);
        clothEditViewModel.setCategoryUrl(clothesServiceModel.getCategoryServiceModel().getCategoryUrl());

        modelAndView.addObject("model", clothEditViewModel);
        return view("offers/clothes/cloth-edit", modelAndView);
    }

    @PostMapping("/clothes/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView clothEditConfirm(@PathVariable String id, ModelAndView modelAndView,
                                         @ModelAttribute (name = "model") ClothesAddBindingModel addBindingModel, BindingResult bindingResult) throws IOException {
        clothesAddValidation.validate(addBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            addBindingModel.setId(id);
            modelAndView.addObject("model", addBindingModel);
            return view("offers/clothes/cloth-edit", modelAndView);
        }
        ClothesServiceModel clothesServiceModel = modelMapper.map(addBindingModel, ClothesServiceModel.class);
        if (!addBindingModel.getImage().isEmpty()) {
            clothesServiceModel.setImage(cloudinaryService.uploadImg(addBindingModel.getImage()));
        } else {
            clothesServiceModel.setImage(NO_IMAGE);
        }
        clothesService.editOffer(clothesServiceModel, id);

        return redirect("/offer/my/all");
    }

    @GetMapping("/clothes/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView clothDelete(@PathVariable String id) {
        clothesService.deleteById(id);

        return redirect("/offer/my/all");
    }


    private void clothesViewSetter(ClothesServiceModel clothesServiceModel, ClothesViewModel model) {
        model.setRegion(clothesServiceModel.getRegion().getName());
        model.setStartsOn(clothesServiceModel.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        model.setEndsOn(clothesServiceModel.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        if (!clothesServiceModel.getDepositNeeded()) {
            model.setDepositNeeded(BOOL_TO_STRING_YES);
        } else {
            model.setDepositNeeded(BOOL_TO_STRING_NO);
        }

    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
