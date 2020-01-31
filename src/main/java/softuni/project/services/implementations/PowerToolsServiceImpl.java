package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.PowerTools;
import softuni.project.data.repositories.PowerToolsRepository;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.interfaces.PowerToolsService;
import softuni.project.services.models.CategoryServiceModel;
import softuni.project.services.models.PowerToolsServiceModel;
import softuni.project.services.validations.PowerToolsServiceModelValidator;

import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.*;

@Service
public class PowerToolsServiceImpl implements PowerToolsService {
    private final static String NOT_SAVED = "Your offer in Power Tools category was not saved, try again!";

    private final PowerToolsRepository powerToolsRepository;
    private final ModelMapper modelMapper;
    private final InitService initService;
    private final PowerToolsServiceModelValidator validator;

    @Autowired
    public PowerToolsServiceImpl(PowerToolsRepository powerToolsRepository, ModelMapper modelMapper,
                                 InitService initService, PowerToolsServiceModelValidator validator) {
        this.powerToolsRepository = powerToolsRepository;
        this.modelMapper = modelMapper;
        this.initService = initService;
        this.validator = validator;
    }

    @Override
    public PowerToolsServiceModel savePowerTools(PowerToolsServiceModel model, String username) {
        if (!validator.isValid(model)){
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        if (model.getIsNeedExtraEquipment() == null) {
            model.setIsNeedExtraEquipment(false);
        }
        if (model.getIsNeedSpecialSkills() == null) {
            model.setIsNeedSpecialSkills(false);
        }
        if (model.getIsPortable() == null) {
            model.setIsPortable(false);
        }
        if (model.getDepositNeeded() == null) {
            model.setDepositNeeded(false);
        }
        model.setIsActive(true);
        model.setIsApproved(false);

        PowerTools powerTools = modelMapper.map(model, PowerTools.class);

        powerTools.setUser(initService.findByUsername(username));
        powerTools.setCategory(initService.findByCategoryName(POWERTOOLS_CATEGORY_NAME));
        return modelMapper.map(powerToolsRepository.saveAndFlush(powerTools), PowerToolsServiceModel.class);
    }

    @Override
    public PowerToolsServiceModel findById(String id) {

        PowerTools powerTools = powerToolsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POWERTOOL_NOT_FOUND));
        PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(powerTools, PowerToolsServiceModel.class);
        powerToolsServiceModel.setCategoryServiceModel(modelMapper.map(powerTools.getCategory(), CategoryServiceModel.class));
        return powerToolsServiceModel;
    }

    @Override
    public List<PowerToolsServiceModel> extractAll() {
        return powerToolsRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc().stream()
                .map(pt -> {
                    PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(pt, PowerToolsServiceModel.class);
                    powerToolsServiceModel.setCategoryServiceModel(modelMapper.map(pt.getCategory(), CategoryServiceModel.class));
                    return powerToolsServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PowerToolsServiceModel> extractAllByUserId(String userId) {
        return powerToolsRepository.findAllByUser_IdOrderByTimeStampDesc(userId).stream()
                .map(c -> {
                    PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(c, PowerToolsServiceModel.class);
                    powerToolsServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return powerToolsServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void editOffer(PowerToolsServiceModel model, String id) {
        if (!validator.isValid(model)){
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }

        powerToolBooleanSetter(model);

        PowerTools powerTools = powerToolsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POWERTOOL_NOT_FOUND));

        powerTools.setName(model.getName());
        powerTools.setDescription(model.getDescription());
        powerTools.setImage(model.getImage() != null ? model.getImage() :
                powerTools.getImage());
        powerTools.setStartsOn(model.getStartsOn());
        powerTools.setEndsOn(model.getEndsOn());
        powerTools.setPrice(model.getPrice());
        powerTools.setRegion(model.getRegion());
        powerTools.setIsActive(true);
        powerTools.setIsApproved(false);
        powerTools.setOwnerPhoneNumber(model.getOwnerPhoneNumber());
        powerTools.setToolCondition(model.getToolCondition());
        powerTools.setDepositNeeded(model.getDepositNeeded());
        powerTools.setIsNeedExtraEquipment(model.getIsNeedExtraEquipment());
        powerTools.setIsNeedSpecialSkills(model.getIsNeedSpecialSkills());
        powerTools.setIsPortable(model.getIsPortable());
        powerToolsRepository.save(powerTools);


    }

    private void powerToolBooleanSetter(PowerToolsServiceModel model) {
        if (model.getDepositNeeded() == null) {
            model.setDepositNeeded(false);
        }
        if (model.getIsNeedSpecialSkills() == null) {
            model.setIsNeedSpecialSkills(false);
        }
        if (model.getIsNeedExtraEquipment() == null) {
            model.setIsNeedExtraEquipment(false);
        }
        if (model.getIsPortable() == null) {
            model.setIsPortable(false);
        }
    }

    @Override
    public void deleteById(String id) {
        powerToolsRepository.deleteById(id);
    }

    @Override
    public List<PowerToolsServiceModel> extractAllForModerator() {
        return powerToolsRepository.findAllByOrderByTimeStampDesc().stream()
                .map(c -> {
                    PowerToolsServiceModel powerToolsServiceModel = modelMapper.map(c, PowerToolsServiceModel.class);
                    powerToolsServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return powerToolsServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeApproveStatus(String id, String isActive) {
        PowerTools powerTools = powerToolsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POWERTOOL_NOT_FOUND));
        if (isActive.equals(TRUE)){
            powerTools.setIsApproved(false);
        }else {
            powerTools.setIsApproved(true);
        }
        powerToolsRepository.save(powerTools);
    }
}
