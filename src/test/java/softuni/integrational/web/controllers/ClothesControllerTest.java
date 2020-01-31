package softuni.integrational.web.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import softuni.integrational.web.controllers.base.ClothesControllerBase;
import softuni.project.web.models.view.ClothesViewModel;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ClothesControllerTest extends ClothesControllerBase {

    @Test
    public void clothesDetails_WhenClothesExist_ReturnView200() throws Exception {
        Mockito.when(clothesRepository.findById("1"))
                .thenReturn(Optional.of(initClothes()));
        Mockito.when(modelMapper.map(initServiceModel(), ClothesViewModel.class))
                .thenReturn(initViewModel());
        mockMvc.perform(get("/offer/clothes/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}
