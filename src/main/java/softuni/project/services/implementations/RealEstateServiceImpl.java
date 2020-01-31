package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.RealEstate;
import softuni.project.data.repositories.RealEstateRepository;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.interfaces.RealEstateService;
import softuni.project.services.models.CategoryServiceModel;
import softuni.project.services.models.RealEstateServiceModel;
import softuni.project.services.validations.RealEstateServiceModelValidator;

import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.*;

@Service
public class RealEstateServiceImpl implements RealEstateService {

    private final static String NOT_SAVED = "Your offer in Real Estates category was not saved, try again!";

    private final RealEstateRepository realEstateRepository;
    private final ModelMapper modelMapper;
    private final InitService initService;
    private final RealEstateServiceModelValidator validator;


    @Autowired
    public RealEstateServiceImpl(RealEstateRepository realEstateRepository, ModelMapper modelMapper,
                                 InitService initService, RealEstateServiceModelValidator validator) {
        this.realEstateRepository = realEstateRepository;
        this.modelMapper = modelMapper;
        this.initService = initService;
        this.validator = validator;
    }

    @Override
    public List<RealEstateServiceModel> extractAll() {
        return realEstateRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc().stream()
                .map(r -> {
                    RealEstateServiceModel realEstateServiceModel = modelMapper.map(r, RealEstateServiceModel.class);
                    realEstateServiceModel.setCategoryServiceModel(modelMapper.map(r.getCategory(), CategoryServiceModel.class));
                    return realEstateServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RealEstateServiceModel findById(String id) {
        RealEstate realEstate = realEstateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REAL_ESTATE_NOT_FOUND));
        realEstate.setExtras(realEstate.getExtras().subList(0, realEstate.getExtras().size() / realEstate.getUser().getAuthorities().size()));
        RealEstateServiceModel realEstateServiceModel = modelMapper.map(realEstate, RealEstateServiceModel.class);
        realEstateServiceModel.setCategoryServiceModel(modelMapper.map(realEstate.getCategory(), CategoryServiceModel.class));
        return realEstateServiceModel;
    }

    @Override
    public RealEstateServiceModel saveRealEstate(RealEstateServiceModel model, String username) {
        if (!validator.isValid(model)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        RealEstate realEstate = modelMapper.map(model, RealEstate.class);
        if (realEstate.getIsPartyFree() == null) {
            realEstate.setIsPartyFree(false);
        }
        realEstate.setIsActive(true);
        realEstate.setIsApproved(false);

        realEstate.setUser(initService.findByUsername(username));
        realEstate.setCategory(initService.findByCategoryName(REALESTATE_CATEGORY_NAME));

        return modelMapper.map(realEstateRepository.saveAndFlush(realEstate), RealEstateServiceModel.class);
    }

    @Override
    public List<RealEstateServiceModel> extractAllByUserId(String userId) {
        return realEstateRepository.findAllByUser_IdOrderByTimeStampDesc(userId).stream()
                .map(c -> {
                    RealEstateServiceModel realEstateServiceModel = modelMapper.map(c, RealEstateServiceModel.class);
                    realEstateServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return realEstateServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void editOffer(RealEstateServiceModel model, String id) {

        if (!validator.isValid(model)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        if (model.getIsPartyFree() == null) {
            model.setIsPartyFree(false);
        }
        RealEstate realEstate = realEstateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REAL_ESTATE_NOT_FOUND));
        realEstate.setName(model.getName());
        realEstate.setDescription(model.getDescription());
        realEstate.setImage(model.getImage() != null ? model.getImage() :
                realEstate.getImage());
        realEstate.setStartsOn(model.getStartsOn());
        realEstate.setEndsOn(model.getEndsOn());
        realEstate.setPrice(model.getPrice());
        realEstate.setRegion(model.getRegion());
        realEstate.setOwnerPhoneNumber(model.getOwnerPhoneNumber());
        realEstate.setIsActive(true);
        realEstate.setIsApproved(false);
        realEstate.setType(model.getType());
        realEstate.setArea(model.getArea());
        realEstate.setExtras(model.getExtras());
        realEstate.setIsPartyFree(model.getIsPartyFree());

        realEstateRepository.save(realEstate);
    }

    @Override
    public void deleteById(String id) {
        realEstateRepository.deleteById(id);
    }

    @Override
    public List<RealEstateServiceModel> extractAllForModerator() {
        return realEstateRepository.findAllByOrderByTimeStampDesc().stream()
                .map(c -> {
                    RealEstateServiceModel serviceModel = modelMapper.map(c, RealEstateServiceModel.class);
                    serviceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return serviceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeApproveStatus(String id, String isActive) {
        RealEstate realEstate = realEstateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REAL_ESTATE_NOT_FOUND));
        if (isActive.equals(TRUE)) {
            realEstate.setIsApproved(false);
        } else {
            realEstate.setIsApproved(true);
        }
        realEstateRepository.save(realEstate);
    }
}
