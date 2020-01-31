package softuni.project.web.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import softuni.project.services.interfaces.ClothesService;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.web.api.models.AllOffersResponseModel;
import softuni.project.web.api.models.ClothResponseModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClothesRestController {

    private final ClothesService clothesService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClothesRestController(ClothesService clothesService, ModelMapper modelMapper) {
        this.clothesService = clothesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all/clothes")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public List<AllOffersResponseModel> getAllClothes() {
        return clothesService.extractAllForModerator().stream()
                .map(c -> {
                    AllOffersResponseModel model = modelMapper.map(c, AllOffersResponseModel.class);
                    model.setCategoryUrl(c.getCategoryServiceModel().getCategoryUrl());
                    return model;
                }).collect(Collectors.toList());
    }
    @GetMapping("/clothes/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ClothResponseModel getAllClothes(@PathVariable String id) {
        ClothesServiceModel cloth =  clothesService.findById(id);
        ClothResponseModel model = modelMapper.map(cloth, ClothResponseModel.class);
        model.setUser(cloth.getUser().getUsername());
        model.setCategoryName(cloth.getCategoryServiceModel().getName());
        return model;
    }

    @PostMapping("/clothes/{id}/{isActive}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void changeApproveStatus(@PathVariable String id, @PathVariable String isActive){
        clothesService.changeApproveStatus(id, isActive);
    }

    @PostMapping("/clothes/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void deleteCloth(@PathVariable String id){
        clothesService.deleteById(id);
    }
}
