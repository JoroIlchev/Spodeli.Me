package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.services.interfaces.*;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.view.CategoryViewModel;
import softuni.project.web.models.view.OfferViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.web.ControllersConstants.CATEGORY_NOT_FOUND_EXCEPTION;
import static softuni.project.web.ControllersConstants.DATE_FORMAT;

@Controller
@RequestMapping("/offer")
public class OfferController extends BaseController {


    private final ModelMapper modelMapper;
    private final RealEstateService realEstateService;
    private final VehicleService vehicleService;
    private final PowerToolsService powerToolsService;
    private final ClothesService clothesService;
    private final CategoryService categoryService;


    @Autowired
    public OfferController(ModelMapper modelMapper, RealEstateService realEstateService,
                           VehicleService vehicleService, PowerToolsService powerToolsService, ClothesService clothesService,
                           CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.realEstateService = realEstateService;
        this.vehicleService = vehicleService;
        this.powerToolsService = powerToolsService;
        this.clothesService = clothesService;
        this.categoryService = categoryService;
    }

    @GetMapping("/category/{url}")
    public ModelAndView showAllOffersInCategory(@PathVariable String url, ModelAndView modelAndView) {
        return getOffersInCategoryView(url, modelAndView);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("All Offers")
    public ModelAndView allCategoriesForFetchChoice(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("offers/offers-all", modelAndView);
    }


    @GetMapping("/my/all")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Offers")
    public ModelAndView myOffers(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("users/my-offers-all", modelAndView);
    }

    private ModelAndView getOffersInCategoryView(String url, ModelAndView modelAndView) {
        List<OfferViewModel> offerViewModelList;
        switch (url) {
            case "clothes":
                offerViewModelList = getAllClothes();
                modelAndView.addObject("offers", offerViewModelList);
                return view("offers/clothes-all", modelAndView);

            case "powertools":
                offerViewModelList = getAllPowerTools();
                modelAndView.addObject("offers", offerViewModelList);
                return view("offers/powertools-all", modelAndView);
            case "realestates":
                offerViewModelList = getAllRealEstates();
                modelAndView.addObject("offers", offerViewModelList);
                return view("offers/realestates-all", modelAndView);
            case "vehicles":
                offerViewModelList = getAllVehicles();
                modelAndView.addObject("offers", offerViewModelList);
                return view("offers/vehicles-all", modelAndView);
            default:
                throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_EXCEPTION);
        }

    }

    private List<OfferViewModel> getAllVehicles() {
        return vehicleService.extractAll().stream()
                .map(v -> {
                    OfferViewModel offerViewModel = modelMapper.map(v, OfferViewModel.class);
                    offerViewModel.setRegion(v.getRegion().getName());
                    offerViewModel.setStartsOn(v.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    offerViewModel.setEndsOn(v.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    return offerViewModel;
                })
                .collect(Collectors.toList());
    }

    private List<OfferViewModel> getAllRealEstates() {
        return realEstateService.extractAll().stream()
                .map(re -> {
                    OfferViewModel offerViewModel = modelMapper.map(re, OfferViewModel.class);
                    offerViewModel.setRegion(re.getRegion().getName());
                    offerViewModel.setStartsOn(re.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    offerViewModel.setEndsOn(re.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    return offerViewModel;
                })
                .collect(Collectors.toList());
    }

    private List<OfferViewModel> getAllPowerTools() {
        return powerToolsService.extractAll().stream()
                .map(pt -> {
                    OfferViewModel offerViewModel = modelMapper.map(pt, OfferViewModel.class);
                    offerViewModel.setRegion(pt.getRegion().getName());
                    offerViewModel.setStartsOn(pt.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    offerViewModel.setEndsOn(pt.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    return offerViewModel;
                })
                .collect(Collectors.toList());
    }

    private List<OfferViewModel> getAllClothes() {
        return clothesService.extractAll().stream()
                .map(c -> {
                    OfferViewModel offerViewModel = modelMapper.map(c, OfferViewModel.class);
                    offerViewModel.setRegion(c.getRegion().getName());
                    offerViewModel.setStartsOn(c.getStartsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    offerViewModel.setEndsOn(c.getEndsOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    return offerViewModel;
                })
                .collect(Collectors.toList());
    }

    private void extractAllCategories(ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels = categoryService.extractAllCategories()
                .stream()
                .map(c -> modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("categories", categoryViewModels);
    }

}
