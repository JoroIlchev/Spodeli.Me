package softuni.integrational.web.controllers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import softuni.integrational.web.controllers.base.OfferControllerBase;
import softuni.project.data.entities.Category;
import softuni.project.data.entities.Clothes;
import softuni.project.data.entities.PowerTools;
import softuni.project.data.entities.RealEstate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class OfferControllerTest extends OfferControllerBase {
    private final static String BASE_VIEW = "offers/";


    @Test
    public void getAllClothes_WhenOk_ReturnClothes200() throws Exception {
        List<Clothes> clothes = new ArrayList<>();
        Mockito.when(clothesRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(clothes);
        mockMvc.perform(get(getUrl("clothes")))
                .andExpect(status().isOk())
                .andExpect(view().name(BASE_VIEW + "clothes-all"));
    }

    @Test
    public void getAllPowerTools_WhenOk_ReturnTools200() throws Exception {
        List<PowerTools> powerToolsList = new ArrayList<>();
        Mockito.when(powerToolsRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(powerToolsList);
        mockMvc.perform(get(getUrl("powertools")))
                .andExpect(status().isOk())
                .andExpect(view().name(BASE_VIEW + "powertools-all"));
    }

    @Test
    public void getAllRealEstates_WhenOk_ReturnRealEstates200() throws Exception {
        List<RealEstate> realEstateList = new ArrayList<>();
        Mockito.when(realEstateRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(realEstateList);
        mockMvc.perform(get(getUrl("realestates")))
                .andExpect(status().isOk())
                .andExpect(view().name(BASE_VIEW + "realestates-all"));
    }
    @Test
    public void getAllVehicles_WhenOk_ReturnVehicles200() throws Exception {
        List<RealEstate> realEstateList = new ArrayList<>();
        Mockito.when(realEstateRepository.findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc())
                .thenReturn(realEstateList);
        mockMvc.perform(get(getUrl("vehicles")))
                .andExpect(status().isOk())
                .andExpect(view().name(BASE_VIEW + "vehicles-all"));
    }

    @Test
    public void getCategoriesForFetchHeader_WhenOk_ReturnVehicles200() throws Exception {
        List<Category> categoryList = new ArrayList<>();
        Mockito.when(categoryRepository.findAll())
                .thenReturn(categoryList);
        mockMvc.perform(get(getUrl()+"all"))
                .andExpect(status().isOk())
                .andExpect(view().name(BASE_VIEW + "offers-all"));
    }
}
