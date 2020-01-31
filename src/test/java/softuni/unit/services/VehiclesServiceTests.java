package softuni.unit.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import softuni.project.data.entities.Category;
import softuni.project.data.entities.User;
import softuni.project.data.entities.Vehicle;
import softuni.project.data.entities.enums.GearBoxType;
import softuni.project.data.entities.enums.Region;
import softuni.project.data.repositories.VehicleRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.implementations.VehicleServiceImpl;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.models.VehicleServiceModel;
import softuni.project.services.validations.VehiclesServiceModelValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class VehiclesServiceTests {
    @InjectMocks
    VehicleServiceImpl service;
    @Mock
    VehicleRepository repository;
    @Mock
    InitService initService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    VehiclesServiceModelValidator validator;
    Vehicle vehicle;
    VehicleServiceModel model;
    User user;
    Category category;
    @Before
    public void initTests() {
        user = new User();
        category = new Category();
        vehicle = new Vehicle();
        model = new VehicleServiceModel();
        model.setName("Name");
        model.setDescription("Description");
        model.setStartsOn(LocalDate.now());
        model.setEndsOn(LocalDate.now());
        model.setPrice(BigDecimal.ZERO);
        model.setRegion(Region.Blagoevgrad);
        model.setOwnerPhoneNumber("0000");
        model.setBrand("Volvo");
        model.setModel("XC60");
        model.setFuel("Diesel");
        model.setGearBox(GearBoxType.Automatic);
        model.setKilometers(1000);
        model.setDrivingLicenseNeeded(true);
        model.setDrivingCategory("B");
    }

    @Test
    public void saveRealEstate_WhenAllValid_ShouldSave() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Vehicle.class))
                .thenReturn(vehicle);
        Mockito.when(modelMapper.map(vehicle, VehicleServiceModel.class))
                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);

        Mockito.when(repository.saveAndFlush(vehicle)).thenReturn(vehicle);

        VehicleServiceModel result = service.saveVehicle(model, "pesho");

        Mockito.verify(modelMapper).map(model, Vehicle.class);
        Mockito.verify(repository).saveAndFlush(vehicle);
        Mockito.verify(modelMapper).map(vehicle, VehicleServiceModel.class);

        assertEquals(model, result);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void saveRealEstates_WhenAllNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        service.saveVehicle(model, "pesho");
    }

    @Test(expected = CategoryNotFoundException.class)
    public void saveRealEstates_WhenCategoryNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Vehicle.class))
                .thenReturn(vehicle);

        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);

        Mockito.when(repository.saveAndFlush(vehicle)).thenThrow(CategoryNotFoundException.class);
        service.saveVehicle(model, "pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void saveRealEstates_WhenUserNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Vehicle.class))
                .thenReturn(vehicle);
        Mockito.when(initService.findByUsername("pesho"))
                .thenThrow(UserNotFoundException.class);
        service.saveVehicle(model, "pesho");
    }

    @Test
    public void extractAll_WhenRealEstatesNotEmpty_ShouldOk() {
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(vehicleList);
        Mockito.when(modelMapper.map(vehicle, VehicleServiceModel.class))
                .thenReturn(model);
        int result = service.extractAll().size();
        assertEquals(vehicleList.size(), result);
    }

    @Test
    public void extractAll_WhenRealEstatesIsEmpty_ShouldOk() {
        List<Vehicle> vehicleList = new ArrayList<>();
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(vehicleList);

        int result = service.extractAll().size();
        assertEquals(vehicleList.size(), result);
    }

    @Test
    public void findById_WhenRealEstatesIdValid_ShouldOk() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(vehicle));
        Mockito.when(modelMapper.map(vehicle, VehicleServiceModel.class))
                .thenReturn(model);
        VehicleServiceModel result = service.findById("id");
        Mockito.verify(repository).findById("id");
        Mockito.verify(modelMapper).map(vehicle, VehicleServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findById_nullInput_throwsIllegalArgumentException() {
        Mockito.when(repository.findById(null))
                .thenThrow(new IllegalArgumentException());
        service.findById(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_invalidId_throwsCategoryNotFoundException() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.empty());
        service.findById("id");
    }

    @Test
    public void extractAllByUserId_WhenUserIdValid_ShouldWork() {
        List<Vehicle> realEstateList = new ArrayList<>();
        realEstateList.add(vehicle);
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc("id"))
                .thenReturn(realEstateList);
        Mockito.when(modelMapper.map(vehicle, VehicleServiceModel.class))
                .thenReturn(model);
        int result = service.extractAllByUserId("id").size();
        assertEquals(realEstateList.size(), result);
    }

    @Test(expected = NullPointerException.class)
    public void extractAllByUserId_WhenUserIdNull_ShouldThrow() {
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc(null))
                .thenThrow(NullPointerException.class);
        service.extractAllByUserId(null);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void editOffer_WhenNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        service.editOffer(model, "id");

    }

    @Test(expected = EntityNotFoundException.class)
    public void editOffer_WhenIdNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenThrow(new EntityNotFoundException());
        service.editOffer(model, "id");
    }

    @Test()
    public void editOffer_WhenValid_ShouldWork() {
        model = mock(VehicleServiceModel.class);
        vehicle = mock(Vehicle.class);
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(vehicle));
        Mockito.when(model.getName()).thenReturn("name");
        Mockito.when(model.getDescription()).thenReturn("description");
        Mockito.when(model.getImage()).thenReturn("img");
        Mockito.when(model.getStartsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getEndsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getPrice()).thenReturn(BigDecimal.ONE);
        Mockito.when(model.getRegion()).thenReturn(Region.Burgas);
        Mockito.when(model.getOwnerPhoneNumber()).thenReturn("123");
        Mockito.when(model.getBrand()).thenReturn("VOLVO");
        Mockito.when(model.getModel()).thenReturn("XC60");
        Mockito.when(model.getFuel()).thenReturn("Diesel");
        Mockito.when(model.getGearBox()).thenReturn(GearBoxType.Automatic);
        Mockito.when(model.getKilometers()).thenReturn(100);
        Mockito.when(model.getDrivingLicenseNeeded()).thenReturn(true);
        Mockito.when(model.getDrivingCategory()).thenReturn("B");

        service.editOffer(model, "id");

        Mockito.verify(vehicle).setName("name");
        Mockito.verify(vehicle).setDescription("description");
        Mockito.verify(vehicle).setImage("img");
        Mockito.verify(vehicle).setStartsOn(LocalDate.now());
        Mockito.verify(vehicle).setEndsOn(LocalDate.now());
        Mockito.verify(vehicle).setPrice(BigDecimal.ONE);
        Mockito.verify(vehicle).setRegion(Region.Burgas);
        Mockito.verify(vehicle).setOwnerPhoneNumber("123");
        Mockito.verify(vehicle).setBrand("VOLVO");
        Mockito.verify(vehicle).setModel("XC60");
        Mockito.verify(vehicle).setFuel("Diesel");
        Mockito.verify(vehicle).setGearBox(GearBoxType.Automatic);
        Mockito.verify(vehicle).setKilometers(100);
        Mockito.verify(vehicle).setDrivingLicenseNeeded(true);
        Mockito.verify(vehicle).setDrivingCategory("B");

        Mockito.verify(repository).save(vehicle);
        Optional<Vehicle> result = repository.findById("id");
        assertEquals(vehicle.getName(), result.get().getName());
    }

    @Test
    public void deleteById_WhenIdIsValid_ShouldWork() {
        String id = "id";
        service.deleteById(id);
        Mockito.verify(repository, times(1)).deleteById(id);
    }


    @Test
    public void extractAllForModerator_WhenThereIsRealEstates_ShouldWork() {
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);
        Mockito.when(modelMapper.map(vehicle, VehicleServiceModel.class))
                .thenReturn(model);
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(vehicleList);
        List<VehicleServiceModel> result = service.extractAllForModerator();
        assertEquals(vehicleList.size(), result.size());
    }

    @Test
    public void extractAllForModerator_WhenThereIsNoRealEstates_ShouldWork() {
        List<Vehicle> vehicleList = new ArrayList<>();
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(vehicleList);
        List<VehicleServiceModel> result = service.extractAllForModerator();
        assertEquals(vehicleList.size(), result.size());
    }

    @Test
    public void changeApproveStatus_WhenTrue_ShouldChangeToFalse() {
        vehicle.setId("id");
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(vehicle));
        service.changeApproveStatus("id", "true");

        Mockito.verify(repository).save(vehicle);
        assertEquals(false, repository.findById("id").get().getIsApproved());
    }

    @Test
    public void changeApproveStatus_WhenFalse_ShouldChangeToTrue() {
        vehicle.setId("id");
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(vehicle));
        service.changeApproveStatus("id", "false");
        Mockito.verify(repository).save(vehicle);
        assertEquals(true, repository.findById("id").get().getIsApproved());
    }

    @Test(expected = EntityNotFoundException.class)
    public void changeApproveStatus_WhenOfferIdNotExist_ShouldThrow() {
        Mockito.when(repository.findById("id"))
                .thenThrow(EntityNotFoundException.class);
        service.changeApproveStatus("id", "true");
    }

}
