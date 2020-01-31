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
import softuni.integrational.TestBase;
import softuni.project.data.entities.Category;
import softuni.project.data.entities.PowerTools;
import softuni.project.data.entities.User;
import softuni.project.data.entities.enums.Region;
import softuni.project.data.repositories.PowerToolsRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.implementations.PowerToolsServiceImpl;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.models.PowerToolsServiceModel;
import softuni.project.services.validations.PowerToolsServiceModelValidator;

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
public class PowerToolsServiceTest extends TestBase {

    @InjectMocks
    PowerToolsServiceImpl powerToolsService;
    @Mock
    PowerToolsRepository repository;
    @Mock
    InitService initService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PowerToolsServiceModelValidator validator;

    PowerTools powerTools;
    PowerToolsServiceModel model;
    User user;
    Category category;

    @Before
    public void initTests() {
        powerTools = new PowerTools();
        model = new PowerToolsServiceModel();
        model.setName("Name");
        model.setDescription("Description");
        model.setStartsOn(LocalDate.now());
        model.setEndsOn(LocalDate.now());
        model.setPrice(BigDecimal.ZERO);
        model.setRegion(Region.Blagoevgrad);
        model.setOwnerPhoneNumber("0000");
        model.setToolCondition("New");

        model.setIsPortable(true);
        model.setIsNeedExtraEquipment(true);
        model.setIsNeedSpecialSkills(true);
        model.setDepositNeeded(true);
        model.setIsActive(true);
        model.setIsApproved(true);
        user = new User();
        category = new Category();

    }

    @Test
    public void savePowerTools_WhenAllValid_ShouldSave() {

        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, PowerTools.class))
                .thenReturn(powerTools);
        Mockito.when(modelMapper.map(powerTools, PowerToolsServiceModel.class))
                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);
        Mockito.when(repository.saveAndFlush(powerTools))
                .thenReturn(powerTools);

        PowerToolsServiceModel result = powerToolsService.savePowerTools(model, "pesho");

        Mockito.verify(modelMapper).map(model, PowerTools.class);
        Mockito.verify(repository).saveAndFlush(powerTools);
        Mockito.verify(modelMapper).map(powerTools, PowerToolsServiceModel.class);

        assertEquals(model, result);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void savePowerTools_WhenAllNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        powerToolsService.savePowerTools(model, "pesho");
    }

    @Test(expected = CategoryNotFoundException.class)
    public void savePowerTools_WhenCategoryNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, PowerTools.class))
                .thenReturn(powerTools);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);
        Mockito.when(repository.saveAndFlush(powerTools))
                .thenThrow(CategoryNotFoundException.class);
       powerToolsService.savePowerTools(model, "pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void savePowerTools_WhenUserNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, PowerTools.class))
                .thenReturn(powerTools);
        Mockito.when(initService.findByUsername("pesho"))
                .thenThrow(UserNotFoundException.class);
        powerToolsService.savePowerTools(model, "pesho");
    }

    @Test
    public void extractAll_WhenPowerToolsNotEmpty_ShouldOk() {
        List<PowerTools> powerToolsList = new ArrayList<>();
        powerToolsList.add(powerTools);
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(powerToolsList);
        Mockito.when(modelMapper.map(powerTools, PowerToolsServiceModel.class))
                .thenReturn(model);
        int result = powerToolsService.extractAll().size();
        assertEquals(powerToolsList.size(), result);
    }

    @Test
    public void extractAll_WhenPowerToolsIsEmpty_ShouldOk() {
        List<PowerTools> powerToolsList = new ArrayList<>();
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(powerToolsList);
        int result = powerToolsService.extractAll().size();
        assertEquals(powerToolsList.size(), result);
    }

    @Test
    public void findById_WhenPowerToolsIdValid_ShouldOk() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(powerTools));
        Mockito.when(modelMapper.map(powerTools, PowerToolsServiceModel.class))
                .thenReturn(model);
        PowerToolsServiceModel result = powerToolsService.findById("id");
        Mockito.verify(repository).findById("id");
        Mockito.verify(modelMapper).map(powerTools, PowerToolsServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findById_nullInput_throwsIllegalArgumentException() {
        Mockito.when(repository.findById(null))
                .thenThrow(new IllegalArgumentException());

        powerToolsService.findById(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_invalidId_throwsCategoryNotFoundException() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.empty());
        powerToolsService.findById("id");
    }

    @Test
    public void extractAllByUserId_WhenUserIdValid_ShouldWork() {
        List<PowerTools> powerToolsList = new ArrayList<>();
        powerToolsList.add(powerTools);
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc("id"))
                .thenReturn(powerToolsList);
        Mockito.when(modelMapper.map(powerTools, PowerToolsServiceModel.class))
                .thenReturn(model);
        int result = powerToolsService.extractAllByUserId("id").size();
        assertEquals(powerToolsList.size(), result);
    }

    @Test(expected = NullPointerException.class)
    public void extractAllByUserId_WhenUserIdNull_ShouldThrow() {
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc(null))
                .thenThrow(NullPointerException.class);
        powerToolsService.extractAllByUserId(null);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void editOffer_WhenNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        powerToolsService.editOffer(model, "id");

    }

    @Test(expected = EntityNotFoundException.class)
    public void editOffer_WhenIdNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenThrow(new EntityNotFoundException());
        powerToolsService.editOffer(model, "id");

    }

    @Test()
    public void editOffer_WhenValid_ShouldWork() {
        model = mock(PowerToolsServiceModel.class);
        powerTools = mock(PowerTools.class);
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(powerTools));
        Mockito.when(model.getName()).thenReturn("name");
        Mockito.when(model.getDescription()).thenReturn("description");
        Mockito.when(model.getImage()).thenReturn("img");
        Mockito.when(model.getStartsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getEndsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getPrice()).thenReturn(BigDecimal.ONE);
        Mockito.when(model.getRegion()).thenReturn(Region.Burgas);
        Mockito.when(model.getOwnerPhoneNumber()).thenReturn("123");
        Mockito.when(model.getToolCondition()).thenReturn("New");
        Mockito.when(model.getIsPortable()).thenReturn(true);
        Mockito.when(model.getIsNeedExtraEquipment()).thenReturn(true);
        Mockito.when(model.getIsNeedSpecialSkills()).thenReturn(true);
        Mockito.when(model.getDepositNeeded()).thenReturn(true);

        powerToolsService.editOffer(model, "id");

        Mockito.verify(powerTools).setName("name");
        Mockito.verify(powerTools).setDescription("description");
        Mockito.verify(powerTools).setImage("img");
        Mockito.verify(powerTools).setStartsOn(LocalDate.now());
        Mockito.verify(powerTools).setEndsOn(LocalDate.now());
        Mockito.verify(powerTools).setPrice(BigDecimal.ONE);
        Mockito.verify(powerTools).setRegion(Region.Burgas);
        Mockito.verify(powerTools).setOwnerPhoneNumber("123");
        Mockito.verify(powerTools).setToolCondition("New");
        Mockito.verify(powerTools).setIsPortable(true);
        Mockito.verify(powerTools).setIsNeedExtraEquipment(true);
        Mockito.verify(powerTools).setIsNeedSpecialSkills(true);
        Mockito.verify(powerTools).setDepositNeeded(true);

        Mockito.verify(repository).save(powerTools);
        Optional<PowerTools> result = repository.findById("id");
        assertEquals(powerTools.getName(), result.get().getName());
    }

    @Test
    public void deleteById_WhenIdIsValid_ShouldWork() {
        String id = "id";
        powerToolsService.deleteById(id);
        Mockito.verify(repository, times(1)).deleteById(id);
    }


    @Test
    public void extractAllForModerator_WhenThereIsTools_ShouldWork() {
        List<PowerTools> powerToolsList = new ArrayList<>();
        powerToolsList.add(powerTools);
        Mockito.when(modelMapper.map(powerTools, PowerToolsServiceModel.class))
                .thenReturn(model);
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(powerToolsList);
        List<PowerToolsServiceModel> result = powerToolsService.extractAllForModerator();
        assertEquals(powerToolsList.size(), result.size());
    }

    @Test
    public void extractAllForModerator_WhenThereIsNoTools_ShouldWork() {
        List<PowerTools> powerToolsList = new ArrayList<>();
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(powerToolsList);
        List<PowerToolsServiceModel> result = powerToolsService.extractAllForModerator();
        assertEquals(powerToolsList.size(), result.size());
    }

    @Test
    public void changeApproveStatus_WhenTrue_ShouldChangeToFalse() {
        powerTools.setId("id");

        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(powerTools));
        powerToolsService.changeApproveStatus("id", "true");

        Mockito.verify(repository).save(powerTools);
        assertEquals(false, repository.findById("id").get().getIsApproved());
    }
    @Test
    public void changeApproveStatus_WhenFalse_ShouldChangeToTrue() {
        powerTools.setId("id");
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(powerTools));
        powerToolsService.changeApproveStatus("id", "false");
        Mockito.verify(repository).save(powerTools);
        assertEquals(true, repository.findById("id").get().getIsApproved());
    }

    @Test(expected = EntityNotFoundException.class)
    public void changeApproveStatus_WhenOfferIdNotExist_ShouldThrow() {
        Mockito.when(repository.findById("id"))
                .thenThrow(EntityNotFoundException.class);
        powerToolsService.changeApproveStatus("id", "true");
    }
}
