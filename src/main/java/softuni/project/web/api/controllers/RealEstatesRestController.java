package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import softuni.project.services.interfaces.RealEstateService;
import softuni.project.services.models.RealEstateServiceModel;
import softuni.project.web.api.models.AllOffersResponseModel;
import softuni.project.web.api.models.RealEstateResponseModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RealEstatesRestController {

    private final RealEstateService realEstateService;
    private final ModelMapper modelMapper;


    @Autowired
    public RealEstatesRestController(RealEstateService realEstateService, ModelMapper modelMapper) {
        this.realEstateService = realEstateService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all/realestates")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public List<AllOffersResponseModel> getAllRealEstatesForModerator() {
        return realEstateService.extractAllForModerator().stream()
                .map(c -> {
                    AllOffersResponseModel model = modelMapper.map(c, AllOffersResponseModel.class);
                    model.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return model;
                }).collect(Collectors.toList());
    }

    @GetMapping("/realestates/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public RealEstateResponseModel getRealEstateOffer(@PathVariable String id) {
        RealEstateServiceModel serviceModel = realEstateService.findById(id);
        RealEstateResponseModel responseModel = modelMapper.map(serviceModel, RealEstateResponseModel.class);
        responseModel.setUser(serviceModel.getUser().getUsername());
        responseModel.setCategoryName(serviceModel.getCategoryServiceModel().getName());
        return responseModel;
    }

    @PostMapping("/realestates/{id}/{isActive}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void changeApproveStatus(@PathVariable String id, @PathVariable String isActive) {
        realEstateService.changeApproveStatus(id, isActive);
    }

    @PostMapping("/realestates/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void deleteRealEstate(@PathVariable String id) {
        realEstateService.deleteById(id);
    }
}
