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
import softuni.project.data.entities.Clothes;
import softuni.project.data.entities.User;
import softuni.project.data.entities.enums.Region;
import softuni.project.data.repositories.ClothesRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.EntityNotFoundException;
import softuni.project.exceptions.EntityNotSavedInDbException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.implementations.ClothesServiceImpl;
import softuni.project.services.interfaces.InitService;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.services.validations.ClothesServiceModelValidator;

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
public class ClothesServiceTest {
    @InjectMocks
    ClothesServiceImpl clothesService;
    @Mock
    ClothesRepository repository;
    @Mock
    InitService initService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    ClothesServiceModelValidator validator;

    Clothes clothes;
    ClothesServiceModel model;
    User user;
    Category category;

    @Before
    public void initTests() {
        clothes = new Clothes();
        model = new ClothesServiceModel();
        model.setName("Name");
        model.setDescription("Description");
        model.setStartsOn(LocalDate.now());
        model.setEndsOn(LocalDate.now());
        model.setPrice(BigDecimal.ZERO);
        model.setRegion(Region.Blagoevgrad);
        model.setOwnerPhoneNumber("0000");
        model.setSize("XXL");
        model.setEntityCondition("New");
        model.setDepositNeeded(true);
        model.setIsActive(true);
        model.setIsApproved(false);
        user = new User();
        category = new Category();
    }


    @Test
    public void saveClothes_WhenAllValid_ShouldSave() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Clothes.class))
                .thenReturn(clothes);
        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);
//        Mockito.when(initService.findByCategoryName("category"))
//                .thenReturn(category);
        Mockito.when(repository.save(clothes)).thenReturn(clothes);

        ClothesServiceModel result = clothesService.saveClothes(model, "pesho");

        Mockito.verify(modelMapper).map(model, Clothes.class);
        Mockito.verify(repository).save(clothes);
        Mockito.verify(modelMapper).map(clothes, ClothesServiceModel.class);

        assertEquals(model, result);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void saveClothes_WhenAllNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        clothesService.saveClothes(model, "pesho");
    }

    @Test(expected = CategoryNotFoundException.class)
    public void saveClothes_WhenCategoryNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Clothes.class))
                .thenReturn(clothes);
//        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
//                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenReturn(user);
//        Mockito.when(initService.findByCategoryName("category"))
//                .thenReturn(category);
        Mockito.when(repository.save(clothes)).thenThrow(CategoryNotFoundException.class);
        ClothesServiceModel result = clothesService.saveClothes(model, "pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void saveClothes_WhenUserNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, Clothes.class))
                .thenReturn(clothes);
//        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
//                .thenReturn(model);
        Mockito.when(initService.findByUsername("pesho"))
                .thenThrow(UserNotFoundException.class);
//        Mockito.when(initService.findByCategoryName("category"))
//                .thenReturn(category);
//        Mockito.when(repository.save(clothes))
//                .thenReturn(clothes);
        ClothesServiceModel result = clothesService.saveClothes(model, "pesho");
    }

    @Test
    public void extractAll_WhenClothesNotEmpty_ShouldOk() {
        List<Clothes> clothesList = new ArrayList<>();
        clothesList.add(clothes);
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(clothesList);
        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
                .thenReturn(model);
        int result = clothesService.extractAll().size();
        assertEquals(clothesList.size(), result);
    }

    @Test
    public void extractAll_WhenClothesIsEmpty_ShouldOk() {
        List<Clothes> clothesList = new ArrayList<>();
        Mockito.when(repository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(clothesList);
//        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
//                .thenReturn(model);
        int result = clothesService.extractAll().size();
        assertEquals(clothesList.size(), result);
    }

    @Test
    public void findById_WhenClothesIdValid_ShouldOk() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(clothes));
        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
                .thenReturn(model);
        ClothesServiceModel result = clothesService.findById("id");
        Mockito.verify(repository).findById("id");
        Mockito.verify(modelMapper).map(clothes, ClothesServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findById_nullInput_throwsIllegalArgumentException() {
        Mockito.when(repository.findById(null))
                .thenThrow(new IllegalArgumentException());

        clothesService.findById(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_invalidId_throwsCategoryNotFoundException() {
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.empty());
        clothesService.findById("id");
    }

    @Test
    public void extractAllByUserId_WhenUserIdValid_ShouldWork() {
        List<Clothes> clothesList = new ArrayList<>();
        clothesList.add(clothes);
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc("id"))
                .thenReturn(clothesList);
        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
                .thenReturn(model);
        int result = clothesService.extractAllByUserId("id").size();
        assertEquals(clothesList.size(), result);
    }

    @Test(expected = NullPointerException.class)
    public void extractAllByUserId_WhenUserIdNull_ShouldThrow() {
        Mockito.when(repository.findAllByUser_IdOrderByTimeStampDesc(null))
                .thenThrow(NullPointerException.class);
        clothesService.extractAllByUserId(null);
    }

    @Test(expected = EntityNotSavedInDbException.class)
    public void editOffer_WhenNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        clothesService.editOffer(model, "id");

    }

    @Test(expected = EntityNotFoundException.class)
    public void editOffer_WhenIdNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenThrow(new EntityNotFoundException());
        clothesService.editOffer(model, "id");

    }

    @Test()
    public void editOffer_WhenValid_ShouldWork() {
        model = mock(ClothesServiceModel.class);
        clothes = mock(Clothes.class);
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(clothes));
        Mockito.when(model.getName()).thenReturn("name");
        Mockito.when(model.getDescription()).thenReturn("description");
        Mockito.when(model.getImage()).thenReturn("img");
        Mockito.when(model.getStartsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getEndsOn()).thenReturn(LocalDate.now());
        Mockito.when(model.getPrice()).thenReturn(BigDecimal.ONE);
        Mockito.when(model.getRegion()).thenReturn(Region.Burgas);
        Mockito.when(model.getSize()).thenReturn("S");
        Mockito.when(model.getEntityCondition()).thenReturn("New");
        Mockito.when(model.getOwnerPhoneNumber()).thenReturn("123");
        Mockito.when(model.getDepositNeeded()).thenReturn(true);

        clothesService.editOffer(model, "id");

        Mockito.verify(clothes).setName("name");
        Mockito.verify(clothes).setDescription("description");
        Mockito.verify(clothes).setImage("img");
        Mockito.verify(clothes).setStartsOn(LocalDate.now());
        Mockito.verify(clothes).setEndsOn(LocalDate.now());
        Mockito.verify(clothes).setPrice(BigDecimal.ONE);
        Mockito.verify(clothes).setRegion(Region.Burgas);
        Mockito.verify(clothes).setSize("S");
        Mockito.verify(clothes).setEntityCondition("New");
        Mockito.verify(clothes).setOwnerPhoneNumber("123");
        Mockito.verify(clothes).setDepositNeeded(true);

        Mockito.verify(repository).save(clothes);
        Optional<Clothes> result = repository.findById("id");
        assertEquals(clothes.getName(), result.get().getName());
    }

    @Test
    public void deleteById_WhenIdIsValid_ShouldWork() {
        String id = "id";
        clothesService.deleteById(id);
        Mockito.verify(repository, times(1)).deleteById(id);
    }


    @Test
    public void extractAllForModerator_WhenThereIsClothes_ShouldWork() {
        List<Clothes> clothesList = new ArrayList<>();
        clothesList.add(clothes);
        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
                .thenReturn(model);
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(clothesList);
        List<ClothesServiceModel> result = clothesService.extractAllForModerator();
        assertEquals(clothesList.size(), result.size());
    }

    @Test
    public void extractAllForModerator_WhenThereIsNoClothes_ShouldWork() {
        List<Clothes> clothesList = new ArrayList<>();
//        Mockito.when(modelMapper.map(clothes, ClothesServiceModel.class))
//                .thenReturn(model);
        Mockito.when(repository.findAllByOrderByTimeStampDesc())
                .thenReturn(clothesList);
        List<ClothesServiceModel> result = clothesService.extractAllForModerator();
        assertEquals(clothesList.size(), result.size());
    }

    @Test
    public void changeApproveStatus_WhenTrue_ShouldChangeToFalse() {
        clothes.setId("id");

        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(clothes));
        clothesService.changeApproveStatus("id", "true");

        Mockito.verify(repository).save(clothes);
        assertEquals(false, repository.findById("id").get().getIsApproved());
    }
    @Test
    public void changeApproveStatus_WhenFalse_ShouldChangeToTrue() {
        clothes.setId("id");
        Mockito.when(repository.findById("id"))
                .thenReturn(Optional.of(clothes));
        clothesService.changeApproveStatus("id", "false");
        Mockito.verify(repository).save(clothes);
        assertEquals(true, repository.findById("id").get().getIsApproved());
    }

    @Test(expected = EntityNotFoundException.class)
    public void changeApproveStatus_WhenOfferIdNotExist_ShouldThrow() {
        Mockito.when(repository.findById("id"))
                .thenThrow(EntityNotFoundException.class);
        clothesService.changeApproveStatus("id", "true");
    }
}
