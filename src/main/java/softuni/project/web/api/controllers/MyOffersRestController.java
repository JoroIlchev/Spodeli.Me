package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softuni.project.services.interfaces.*;
import softuni.project.services.models.UserServiceModel;
import softuni.project.web.api.models.MyOfferResponseModel;
import softuni.project.web.base.BaseController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.web.ControllersConstants.*;

@RestController
@RequestMapping("/api")
public class MyOffersRestController extends BaseController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ClothesService clothesService;
    private final PowerToolsService powerToolsService;
    private final RealEstateService realEstateService;
    private final VehicleService vehicleService;

    public MyOffersRestController(ModelMapper modelMapper, UserService userService, ClothesService clothesService, PowerToolsService powerToolsService, RealEstateService realEstateService, VehicleService vehicleService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.clothesService = clothesService;
        this.powerToolsService = powerToolsService;
        this.realEstateService = realEstateService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("allOffers/all")
    @PreAuthorize("isAuthenticated()")
    public List<MyOfferResponseModel> getAllMyOffers(Principal principal) {
        String username = getUsername(principal);
        UserServiceModel userServiceModel = userService.findByUsername(username);
        String userId = userServiceModel.getId();
        return allMyOffers(userId);
    }

    @GetMapping("/clothes/all")
    @PreAuthorize("isAuthenticated()")
    public List<MyOfferResponseModel> getMyClothes(Principal principal) {
        String username = getUsername(principal);
        UserServiceModel userServiceModel = userService.findByUsername(username);
        String userId = userServiceModel.getId();
        return getAllClothes(userId);
    }


    @GetMapping("/powertools/all")
    @PreAuthorize("isAuthenticated()")
    public List<MyOfferResponseModel> getMyPowerTools(Principal principal) {
        String username = getUsername(principal);
        UserServiceModel userServiceModel = userService.findByUsername(username);
        String userId = userServiceModel.getId();
        return getAllPowerTools(userId);
    }


    @GetMapping("/realestates/all")
    @PreAuthorize("isAuthenticated()")
    public List<MyOfferResponseModel> getMyRealEstates(Principal principal) {
        String username = getUsername(principal);
        UserServiceModel userServiceModel = userService.findByUsername(username);
        String userId = userServiceModel.getId();
        return getAllRealEstates(userId);
    }

    @GetMapping("/vehicles/all")
    @PreAuthorize("isAuthenticated()")
    public List<MyOfferResponseModel> getMyVehicles(Principal principal) {
        String username = getUsername(principal);
        UserServiceModel userServiceModel = userService.findByUsername(username);
        String userId = userServiceModel.getId();
        return getAllVehicles(userId);
    }

    private List<MyOfferResponseModel> allMyOffers(String userId) {
        List<MyOfferResponseModel> myOffers = getAllClothes(userId);

        myOffers.addAll(getAllPowerTools(userId));

        myOffers.addAll(getAllRealEstates(userId));

        myOffers.addAll(getAllVehicles(userId));

        return myOffers;
    }

    private List<MyOfferResponseModel> getAllClothes(String userId) {
        return clothesService.extractAllByUserId(userId)
                .stream()
                .map(c -> {
                    MyOfferResponseModel myOfferResponseModel = modelMapper.map(c, MyOfferResponseModel.class);
                    myOfferResponseModel.setIsActive(c.getIsActive() ? BOOL_TO_STRING_YES : BOOL_TO_STRING_NO);
                    myOfferResponseModel.setIsApproved(c.getIsApproved() ? BOOL_TO_STRING_YES : WAITING_FOR_APPROVING);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList());
    }

    private List<MyOfferResponseModel> getAllPowerTools(String userId) {
        return powerToolsService.extractAllByUserId(userId)
                .stream()
                .map(c -> {
                    MyOfferResponseModel myOfferResponseModel = modelMapper.map(c, MyOfferResponseModel.class);
                    myOfferResponseModel.setIsActive(c.getIsActive() ? BOOL_TO_STRING_YES : BOOL_TO_STRING_NO);
                    myOfferResponseModel.setIsApproved(c.getIsApproved() ? BOOL_TO_STRING_YES : WAITING_FOR_APPROVING);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList());
    }

    private List<MyOfferResponseModel> getAllRealEstates(String userId) {
        return realEstateService.extractAllByUserId(userId)
                .stream()
                .map(c -> {
                    MyOfferResponseModel myOfferResponseModel = modelMapper.map(c, MyOfferResponseModel.class);
                    myOfferResponseModel.setIsActive(c.getIsActive() ? BOOL_TO_STRING_YES : BOOL_TO_STRING_NO);
                    myOfferResponseModel.setIsApproved(c.getIsApproved() ? BOOL_TO_STRING_YES : WAITING_FOR_APPROVING);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList());
    }

    private List<MyOfferResponseModel> getAllVehicles(String userId) {
        return vehicleService.extractAllByUserId(userId)
                .stream()
                .map(c -> {
                    MyOfferResponseModel myOfferResponseModel = modelMapper.map(c, MyOfferResponseModel.class);
                    myOfferResponseModel.setIsActive(c.getIsActive() ? BOOL_TO_STRING_YES : BOOL_TO_STRING_NO);
                    myOfferResponseModel.setIsApproved(c.getIsApproved() ? BOOL_TO_STRING_YES : WAITING_FOR_APPROVING);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList());
    }


}
