package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Vehicle;
import softuni.project.data.repositories.VehicleRepository;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.interfaces.VehicleService;
import softuni.project.services.models.CategoryServiceModel;
import softuni.project.services.models.VehicleServiceModel;
import softuni.project.services.validations.VehiclesServiceModelValidator;

import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.*;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final static String NOT_SAVED = "Your offer in Vehicles category was not saved, try again!";

    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;
    private final InitService initService;
    private final VehiclesServiceModelValidator validator;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, ModelMapper modelMapper,
                              InitService initService, VehiclesServiceModelValidator validator) {
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
        this.initService = initService;
        this.validator = validator;
    }

    @Override
    public VehicleServiceModel saveVehicle(VehicleServiceModel model, String username) {
        if (!validator.isValid(model)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        Vehicle vehicle = modelMapper.map(model, Vehicle.class);
        if (vehicle.getDrivingLicenseNeeded() == null) {
            vehicle.setDrivingLicenseNeeded(false);
        }
        vehicle.setIsActive(true);
        vehicle.setIsApproved(false);
        vehicle.setUser(initService.findByUsername(username));
        vehicle.setCategory(initService.findByCategoryName(VEHICLES_CATEGORY_NAME));

        return modelMapper.map(vehicleRepository.saveAndFlush(vehicle), VehicleServiceModel.class);
    }

    @Override
    public VehicleServiceModel findById(String id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE_NOT_FOUND));
        VehicleServiceModel vehicleServiceModel = modelMapper.map(vehicle, VehicleServiceModel.class);
        vehicleServiceModel.setCategoryServiceModel(modelMapper.map(vehicle.getCategory(), CategoryServiceModel.class));
        return vehicleServiceModel;
    }

    @Override
    public List<VehicleServiceModel> extractAll() {
        return vehicleRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc().stream()
                .map(v -> {
                    VehicleServiceModel vehicleServiceModel = modelMapper.map(v, VehicleServiceModel.class);
                    vehicleServiceModel.setCategoryServiceModel(modelMapper.map(v.getCategory(), CategoryServiceModel.class));
                    return vehicleServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleServiceModel> extractAllByUserId(String userId) {
        return vehicleRepository.findAllByUser_IdOrderByTimeStampDesc(userId).stream()
                .map(c -> {
                    VehicleServiceModel vehicleServiceModel = modelMapper.map(c, VehicleServiceModel.class);
                    vehicleServiceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return vehicleServiceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void editOffer(VehicleServiceModel model, String id) {
        if (!validator.isValid(model)) {
            throw new EntityNotSavedInDbException(NOT_SAVED);
        }
        if (model.getDrivingLicenseNeeded() == null) {
            model.setDrivingLicenseNeeded(false);
        }
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE_NOT_FOUND));

        vehicle.setName(model.getName());
        vehicle.setDescription(model.getDescription());
        vehicle.setImage(model.getImage() != null ? model.getImage() :
                vehicle.getImage());
        vehicle.setStartsOn(model.getStartsOn());
        vehicle.setEndsOn(model.getEndsOn());
        vehicle.setPrice(model.getPrice());
        vehicle.setRegion(model.getRegion());
        vehicle.setIsActive(true);
        vehicle.setIsApproved(false);
        vehicle.setOwnerPhoneNumber(model.getOwnerPhoneNumber());
        vehicle.setBrand(model.getBrand());
        vehicle.setModel(model.getModel());
        vehicle.setFuel(model.getFuel());
        vehicle.setGearBox(model.getGearBox());
        vehicle.setKilometers(model.getKilometers());
        vehicle.setDrivingLicenseNeeded(model.getDrivingLicenseNeeded());
        vehicle.setDrivingCategory(model.getDrivingCategory());

        vehicleRepository.save(vehicle);

    }

    @Override
    public void deleteById(String id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleServiceModel> extractAllForModerator() {
        return vehicleRepository.findAllByOrderByTimeStampDesc().stream()
                .map(c -> {
                    VehicleServiceModel serviceModel = modelMapper.map(c, VehicleServiceModel.class);
                    serviceModel.setCategoryServiceModel(modelMapper.map(c.getCategory(), CategoryServiceModel.class));
                    return serviceModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeApproveStatus(String id, String isActive) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE_NOT_FOUND));
        if (isActive.equals(TRUE)) {
            vehicle.setIsApproved(false);
        } else {
            vehicle.setIsApproved(true);
        }
        vehicleRepository.save(vehicle);
    }
}
