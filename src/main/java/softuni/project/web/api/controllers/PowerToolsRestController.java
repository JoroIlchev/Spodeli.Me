package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import softuni.project.services.interfaces.PowerToolsService;
import softuni.project.services.models.PowerToolsServiceModel;
import softuni.project.web.api.models.AllOffersResponseModel;
import softuni.project.web.api.models.PowerToolResponseModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PowerToolsRestController {

    private final PowerToolsService powerToolsService;
    private final ModelMapper modelMapper;

    @Autowired
    public PowerToolsRestController(PowerToolsService powerToolsService, ModelMapper modelMapper) {
        this.powerToolsService = powerToolsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all/powertools")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public List<AllOffersResponseModel> getAllPowerToolsForModerator() {
        return powerToolsService.extractAllForModerator().stream()
                .map(c -> {
                    AllOffersResponseModel model = modelMapper.map(c, AllOffersResponseModel.class);
                    model.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return model;
                }).collect(Collectors.toList());
    }
    @GetMapping("/powertools/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public PowerToolResponseModel getPowerToolOffer(@PathVariable String id) {
        PowerToolsServiceModel serviceModel =  powerToolsService.findById(id);
        PowerToolResponseModel responseModel = modelMapper.map(serviceModel, PowerToolResponseModel.class);
        responseModel.setUser(serviceModel.getUser().getUsername());
        responseModel.setCategoryName(serviceModel.getCategoryServiceModel().getName());
        return responseModel;
    }

    @PostMapping("/powertools/{id}/{isActive}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void changeApproveStatus(@PathVariable String id, @PathVariable String isActive){
        powerToolsService.changeApproveStatus(id, isActive);
    }

    @PostMapping("/powertools/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void deletePowerTool(@PathVariable String id){
        powerToolsService.deleteById(id);
    }
}
