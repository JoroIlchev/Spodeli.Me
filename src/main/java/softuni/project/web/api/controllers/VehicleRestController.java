package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import softuni.project.services.interfaces.VehicleService;
import softuni.project.services.models.VehicleServiceModel;
import softuni.project.web.api.models.AllOffersResponseModel;
import softuni.project.web.api.models.VehicleResponseModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class VehicleRestController {

    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleRestController(VehicleService vehicleService, ModelMapper modelMapper) {
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all/vehicles")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public List<AllOffersResponseModel> getAllRealEstatesForModerator() {
        return vehicleService.extractAllForModerator().stream()
                .map(c -> {
                    AllOffersResponseModel model = modelMapper.map(c, AllOffersResponseModel.class);
                    model.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return model;
                }).collect(Collectors.toList());
    }

    @GetMapping("/vehicles/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public VehicleResponseModel getRealEstateOffer(@PathVariable String id) {
        VehicleServiceModel serviceModel = vehicleService.findById(id);
        VehicleResponseModel responseModel = modelMapper.map(serviceModel, VehicleResponseModel.class);
        responseModel.setUser(serviceModel.getUser().getUsername());
        responseModel.setCategoryName(serviceModel.getCategoryServiceModel().getName());
        return responseModel;
    }

    @PostMapping("/vehicles/{id}/{isActive}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void changeApproveStatus(@PathVariable String id, @PathVariable String isActive) {
        vehicleService.changeApproveStatus(id, isActive);
    }

    @PostMapping("/vehicles/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void deleteRealEstate(@PathVariable String id) {
        vehicleService.deleteById(id);
    }
}
