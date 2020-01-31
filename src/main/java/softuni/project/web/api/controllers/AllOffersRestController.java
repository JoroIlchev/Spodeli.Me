package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softuni.project.services.interfaces.ClothesService;
import softuni.project.services.interfaces.PowerToolsService;
import softuni.project.services.interfaces.RealEstateService;
import softuni.project.services.interfaces.VehicleService;
import softuni.project.web.api.models.AllOffersResponseModel;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AllOffersRestController {

    private final ModelMapper modelMapper;
    private final ClothesService clothesService;
    private final PowerToolsService powerToolsService;
    private final RealEstateService realEstateService;
    private final VehicleService vehicleService;

    @Autowired
    public AllOffersRestController(ModelMapper modelMapper, ClothesService clothesService,
                                   PowerToolsService powerToolsService, RealEstateService realEstateService,
                                   VehicleService vehicleService) {
        this.modelMapper = modelMapper;
        this.clothesService = clothesService;
        this.powerToolsService = powerToolsService;
        this.realEstateService = realEstateService;
        this.vehicleService = vehicleService;
    }


    @GetMapping("/all/allOffers")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public List<AllOffersResponseModel> getAllOffers(Principal principal) {
        return extractAllOffersFormDb();
    }

    private List<AllOffersResponseModel> extractAllOffersFormDb() {
        List<AllOffersResponseModel> allOffers = clothesService.extractAllForModerator()
                .stream()
                .map(c -> {
                    AllOffersResponseModel myOfferResponseModel = modelMapper.map(c, AllOffersResponseModel.class);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList());

        allOffers.addAll(powerToolsService.extractAllForModerator()
                .stream()
                .map(c -> {
                    AllOffersResponseModel myOfferResponseModel = modelMapper.map(c, AllOffersResponseModel.class);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList()));

        allOffers.addAll(realEstateService.extractAllForModerator()
                .stream()
                .map(c -> {
                    AllOffersResponseModel myOfferResponseModel = modelMapper.map(c, AllOffersResponseModel.class);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList()));

        allOffers.addAll(vehicleService.extractAllForModerator()
                .stream()
                .map(c -> {
                    AllOffersResponseModel myOfferResponseModel = modelMapper.map(c, AllOffersResponseModel.class);
                    myOfferResponseModel.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return myOfferResponseModel;
                })
                .collect(Collectors.toList()));

        return allOffers;
    }
}
