package softuni.integrational.web.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import softuni.integrational.web.controllers.base.UserControllerBase;
import softuni.project.data.entities.Role;
import softuni.project.data.entities.User;
import softuni.project.services.models.RoleServiceModel;
import softuni.project.services.models.UserServiceModel;
import softuni.project.web.models.binding.UserRegisterBindingModel;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends UserControllerBase {

    @Test
    public void registerGET() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserControllerBase.REGISTER_VIEW));
    }

    @Test
    public void registerConfirm_WhenUserOk_Error() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("name");
        userRegisterBindingModel.setPassword("1");
        userRegisterBindingModel.setEmail("email");
        // user.setAuthorities(Set.of(new Role()));
        User user = new User();
        user.setUsername("name");
        user.setPassword("1");
        user.setEmail("email");
        user.setAuthorities(Set.of(new Role()));
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("name");
        userServiceModel.setPassword("1");
        userServiceModel.setEmail("email");
        userServiceModel.setAuthorities(Set.of(new RoleServiceModel()));
        Mockito.when(modelMapper.map(userRegisterBindingModel, UserServiceModel.class))
                .thenReturn(userServiceModel);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);

        mockMvc.perform(post("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void getDetails_whenUserWithName_shouldReturnLogIn302() throws Exception {
        mockMvc.perform(get("/users/edit"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl(UserControllerBase.REGISTER_VIEW));
    }

}