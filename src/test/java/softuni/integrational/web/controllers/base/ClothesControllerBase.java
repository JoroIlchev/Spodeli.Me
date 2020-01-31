package softuni.integrational.web.controllers.base;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.project.data.entities.Clothes;
import softuni.project.data.entities.enums.Region;
import softuni.project.data.repositories.ClothesRepository;
import softuni.project.services.interfaces.ClothesService;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.validations.offers.ClothesAddValidation;
import softuni.project.web.models.view.ClothesViewModel;

import java.time.LocalDate;

public class ClothesControllerBase extends WebBaseTest {
    @MockBean
    protected ClothesRepository clothesRepository;
    @MockBean
    protected ClothesService clothesService;
    @MockBean
    protected ModelMapper modelMapper;
    @MockBean
    protected CloudinaryService cloudinaryService;
    @MockBean
    protected ClothesAddValidation clothesAddValidation;

    Clothes clothes;
    ClothesServiceModel clothesServiceModel;
    ClothesViewModel viewModel;


    protected Clothes initClothes() {
        clothes = new Clothes();
        clothes.setId("1");
        return clothes;
    }

    protected ClothesServiceModel initServiceModel() {
        clothesServiceModel = new ClothesServiceModel();
        clothesServiceModel.setRegion(Region.Blagoevgrad);
        clothesServiceModel.setStartsOn(LocalDate.now());
        clothesServiceModel.setEndsOn(LocalDate.now());
        clothesServiceModel.setDepositNeeded(true);
        return clothesServiceModel;
    }

    protected ClothesViewModel initViewModel() {
        viewModel = new ClothesViewModel();
        viewModel.setRegion(Region.Blagoevgrad.getName());
        viewModel.setStartsOn("2019.12.12");
        viewModel.setEndsOn("2019.12.12");
        viewModel.setDepositNeeded("Да");
        return viewModel;
    }
}
