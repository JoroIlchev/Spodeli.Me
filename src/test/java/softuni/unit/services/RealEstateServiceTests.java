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
import softuni.project.data.entities.RealEstate;
import softuni.project.data.entities.Role;
import softuni.project.data.entities.User;
import softuni.project.data.entities.enums.Extras;
import softuni.project.data.entities.enums.Region;
import softuni.project.data.entities.enums.TypeOfRealEstate;
import softuni.project.data.repositories.RealEstateRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.implementations.RealEstateServiceImpl;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.models.RealEstateServiceModel;
import softuni.project.services.validations.RealEstateServiceModelValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RealEstateServiceTests {
    @InjectMocks
    RealEstateServiceImpl service;
    @Mock
    RealEstateRepository repository;
    @Mock
    InitService initService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    RealEstateServiceModelValidator validator;

    RealEstate realEstate;
    RealEstateServiceModel model;
    User user;
    Category category;

    @Before
    public void initTests() {
        user = new User();
        category = new Category();
        realEstate = new RealEstate();
        model = new RealEstateServiceModel();
        model.setName("Name");
        model.setDescription("Description");
        model.setStartsOn(LocalDate.now());
        model.setEndsOn(LocalDate.now());
        model.setPrice(BigDecimal.ZERO);
        model.setRegion(Region.Blagoevgrad);
        model.setOwnerPhoneNumber("0000");
        model.setType(TypeOfRealEstate.Апартамент);
        model.setArea(2.22);
        model.setExtras(List.of(Extras.Асансьор));
        model.setIsPartyFree(true);
    }

    @Test
    public void saveRealEstate_WhenAllValid_ShouldSave() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, RealEstate.class))
                .thenReturn(realEstate);
        Mockito.when(modelMapper.map(realEstate, RealEstateServiceModel.class))
                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);

        Mockito.when(repository.saveAndFlush(realEstate)).thenReturn(realEstate);

        RealEstateServiceModel result = service.saveRealEstate(model, "pesho");

        Mockito.verify(modelMapper).map(model, RealEstate.class);
        Mockito.verify(repository).saveAndFlush(realEstate);
        Mockito.verify(modelMapper).map(realEstate, RealEstateServiceModel.class);

        assertEquals(model, result);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void saveRealEstates_WhenAllNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        service.saveRealEstate(model, "pesho");
    }

    @Test(expected = CategoryNotFoundException.class)
    public void saveRealEstates_WhenCategoryNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, RealEstate.class))
                .thenReturn(realEstate);

        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);

        Mockito.when(repository.saveAndFlush(realEstate)).thenThrow(CategoryNotFoundException.class);
        service.saveRealEstate(model, "pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void saveRealEstates_WhenUserNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, RealEstate.class))
                .thenReturn(realEstate);
        Mockito.when(initService.findByUsername("pesho"))
                .thenThrow(UserNotFoundException.class);
        service.saveRealEstate(model, "pesho");
    }

    @Test
    public void extractAll_WhenRealEstatesNotEmpty_ShouldOk() {
        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add(realEstate);
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(realEstateList);
        Mockito.when(modelMapper.map(realEstate, RealEstateServiceModel.class))
                .thenReturn(model);
        int result = service.extractAll().size();
        assertEquals(realEstateList.size(), result);
    }

    @Test
    public void extractAll_WhenRealEstatesIsEmpty_ShouldOk() {
        List<RealEstate> realEstateList = new ArrayList<>();
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(realEstateList);

        int result = service.extractAll().size();
        assertEquals(realEstateList.size(), result);
    }

    @Test
    public void findById_WhenRealEstatesIdValid_ShouldOk() {
        realEstate.setExtras(List.of(Extras.Асансьор));
        user.setAuthorities(Set.of(new Role()));
        realEstate.setUser(user);
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(realEstate));
        Mockito.when(modelMapper.map(realEstate, RealEstateServiceModel.class))
                .thenReturn(model);
        RealEstateServiceModel result = service.findById("id");
        Mockito.verify(repository).findById("id");
        Mockito.verify(modelMapper).map(realEstate, RealEstateServiceModel.class);
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
        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add(realEstate);
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc("id"))
                .thenReturn(realEstateList);
        Mockito.when(modelMapper.map(realEstate, RealEstateServiceModel.class))
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
        model = mock(RealEstateServiceModel.class);
        realEstate = mock(RealEstate.class);
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(realEstate));
        Mockito.when(model.getName()).thenReturn("name");
        Mockito.when(model.getDescription()).thenReturn("description");
        Mockito.when(model.getImage()).thenReturn("img");
        Mockito.when(model.getStartsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getEndsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getPrice()).thenReturn(BigDecimal.ONE);
        Mockito.when(model.getRegion()).thenReturn(Region.Burgas);
        Mockito.when(model.getOwnerPhoneNumber()).thenReturn("123");
        Mockito.when(model.getType()).thenReturn(TypeOfRealEstate.Апартамент);
        Mockito.when(model.getArea()).thenReturn(3.33);
        Mockito.when(model.getExtras()).thenReturn(List.of(Extras.Асансьор));
        Mockito.when(model.getIsPartyFree()).thenReturn(true);

        service.editOffer(model, "id");

        Mockito.verify(realEstate).setName("name");
        Mockito.verify(realEstate).setDescription("description");
        Mockito.verify(realEstate).setImage("img");
        Mockito.verify(realEstate).setStartsOn(LocalDate.now());
        Mockito.verify(realEstate).setEndsOn(LocalDate.now());
        Mockito.verify(realEstate).setPrice(BigDecimal.ONE);
        Mockito.verify(realEstate).setRegion(Region.Burgas);
        Mockito.verify(realEstate).setOwnerPhoneNumber("123");
        Mockito.verify(realEstate).setType(TypeOfRealEstate.Апартамент);
        Mockito.verify(realEstate).setArea(3.33);
        Mockito.verify(realEstate).setExtras(List.of(Extras.Асансьор));
        Mockito.verify(realEstate).setIsPartyFree(true);



        Mockito.verify(repository).save(realEstate);
        Optional<RealEstate> result = repository.findById("id");
        assertEquals(realEstate.getName(), result.get().getName());
    }

    @Test
    public void deleteById_WhenIdIsValid_ShouldWork() {
        String id = "id";
        service.deleteById(id);
        Mockito.verify(repository, times(1)).deleteById(id);
    }


    @Test
    public void extractAllForModerator_WhenThereIsRealEstates_ShouldWork() {
        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add(realEstate);
        Mockito.when(modelMapper.map(realEstate, RealEstateServiceModel.class))
                .thenReturn(model);
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(realEstateList);
        List<RealEstateServiceModel> result = service.extractAllForModerator();
        assertEquals(realEstateList.size(), result.size());
    }

    @Test
    public void extractAllForModerator_WhenThereIsNoRealEstates_ShouldWork() {
        List<RealEstate> realEstateList = new ArrayList<>();
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(realEstateList);
        List<RealEstateServiceModel> result = service.extractAllForModerator();
        assertEquals(realEstateList.size(), result.size());
    }

    @Test
    public void changeApproveStatus_WhenTrue_ShouldChangeToFalse() {
        realEstate.setId("id");

        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(realEstate));
        service.changeApproveStatus("id", "true");

        Mockito.verify(repository).save(realEstate);
        assertEquals(false, repository.findById("id").get().getIsApproved());
    }

    @Test
    public void changeApproveStatus_WhenFalse_ShouldChangeToTrue() {
        realEstate.setId("id");
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(realEstate));
        service.changeApproveStatus("id", "false");
        Mockito.verify(repository).save(realEstate);
        assertEquals(true, repository.findById("id").get().getIsApproved());
    }

    @Test(expected = EntityNotFoundException.class)
    public void changeApproveStatus_WhenOfferIdNotExist_ShouldThrow() {
        Mockito.when(repository.findById("id"))
                .thenThrow(EntityNotFoundException.class);
        service.changeApproveStatus("id", "true");
    }

}
