package softuni.integrational.web.controllers.base;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.project.data.repositories.*;
import softuni.project.services.interfaces.*;

public class OfferControllerBase extends WebBaseTest {

    @MockBean
    ModelMapper modelMapper;
    @MockBean
    protected RealEstateRepository realEstateRepository;
    @MockBean
    protected VehicleRepository vehicleRepository;
    @MockBean
    protected PowerToolsRepository powerToolsRepository;
    @MockBean
    protected ClothesRepository clothesRepository;
    @MockBean
    protected ClothesService clothesService;
    @MockBean
    protected CategoryRepository categoryRepository;

    protected String getUrl(String category) {
        return "/offer/category/" + category;
    }

    protected String getUrl() {
        return "/offer/";
    }
}
