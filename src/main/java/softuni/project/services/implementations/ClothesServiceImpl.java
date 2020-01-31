package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Clothes;
import softuni.project.data.repositories.ClothesRepository;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.services.interfaces.ClothesService;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.models.CategoryServiceModel;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.services.validations.ClothesServiceModelValidator;

import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.*;

@Service
public class ClothesServiceImpl implements ClothesService {

    private final static String NOT_SAVED = "Your offer in Clothes category was not saved, try again!";

    private final ClothesRepository clothesRepository;
    private final ModelMapper modelMapper;
    private final InitService initService;
    private final ClothesServiceModelValidator validator;

    @Autowired
    public ClothesServiceImpl(ClothesRepository clothesRepository, ModelMapper modelMapper, InitService initService, ClothesServiceModelValidator validator) {
        this.clothesRepository = clothesRepository;
        this.modelMapper = modelMapper;
        this.initService = initService;
        this.validator = validator;
    }

    @Override
    public ClothesServiceModel saveClothes(ClothesServiceModel model, String username) {
        if (!validator.isValid(model)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        if (model.getDepositNeeded() == null) {
            model.setDepositNeeded(false);
        }
        model.setIsActive(true);
        model.setIsApproved(false);

        Clothes clothes = modelMapper.map(model, Clothes.class);

        clothes.setUser(initService.findByUsername(username));
        clothes.setCategory(initService.findByCategoryName(CLOTHES_CATEGORY_NAME));
        return modelMapper.map(clothesRepository.save(clothes), ClothesServiceModel.class);
    }

    @Override
    public List<ClothesServiceModel> extractAll() {
        return clothesRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc().stream()
                .map(c -> {
                    ClothesServiceModel clothesServiceModel = modelMapper.map(c, ClothesServiceModel.class);
                    clothesServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return clothesServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ClothesServiceModel findById(String id) {
        Clothes cloth = clothesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLOTH_NOT_FOUND));
        ClothesServiceModel clothesServiceModel = modelMapper.map(cloth, ClothesServiceModel.class);
        clothesServiceModel.setCategoryServiceModel(modelMapper.map(cloth.getCategory(), CategoryServiceModel.class));
        return clothesServiceModel;
    }

    @Override
    public List<ClothesServiceModel> extractAllByUserId(String userId) {

        return clothesRepository.findAllByUser_IdOrderByTimeStampDesc(userId)
                .stream()
                .map(c -> {
                    ClothesServiceModel clothesServiceModel = modelMapper.map(c, ClothesServiceModel.class);
                    clothesServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return clothesServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void editOffer(ClothesServiceModel clothesServiceModel, String id) {
        if (!validator.isValid(clothesServiceModel)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        if (clothesServiceModel.getDepositNeeded() == null) {
            clothesServiceModel.setDepositNeeded(false);
        }

        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLOTH_NOT_FOUND));

        clothes.setName(clothesServiceModel.getName());
        clothes.setDescription(clothesServiceModel.getDescription());
        clothes.setImage(clothesServiceModel.getImage() != null ? clothesServiceModel.getImage() :
                clothes.getImage());
        clothes.setStartsOn(clothesServiceModel.getStartsOn());
        clothes.setEndsOn(clothesServiceModel.getEndsOn());
        clothes.setPrice(clothesServiceModel.getPrice());
        clothes.setRegion(clothesServiceModel.getRegion());
        clothes.setIsActive(true);
        clothes.setIsApproved(false);
        clothes.setSize(clothesServiceModel.getSize());
        clothes.setEntityCondition(clothesServiceModel.getEntityCondition());
        clothes.setOwnerPhoneNumber(clothesServiceModel.getOwnerPhoneNumber());
        clothesRepository.save(clothes);
    }

    @Override
    public void deleteById(String id) {
        clothesRepository.deleteById(id);
    }

    @Override
    public List<ClothesServiceModel> extractAllForModerator() {
        return clothesRepository.findAllByOrderByTimeStampDesc().stream()
                .map(c -> {
                    ClothesServiceModel clothesServiceModel = modelMapper.map(c, ClothesServiceModel.class);
                    clothesServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return clothesServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeApproveStatus(String id, String isActive) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLOTH_NOT_FOUND));
//        Thats mean offer is now active and moderator changed to NOT active
        if (isActive.equals(TRUE)) {
            clothes.setIsApproved(false);
        } else {
            clothes.setIsApproved(true);
        }
        clothesRepository.save(clothes);
    }

}
