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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import softuni.project.data.entities.User;
import softuni.project.data.repositories.UserRepository;
import softuni.project.exceptions.UserNameTakenException;
import softuni.project.exceptions.UserWrongCredentialsException;
import softuni.project.services.implementations.UserServiceImpl;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.services.interfaces.RoleService;
import softuni.project.services.models.RoleServiceModel;
import softuni.project.services.models.UserServiceModel;
import softuni.project.services.validations.UserServiceModelValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTests {
    @InjectMocks
    UserServiceImpl service;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleService roleService;
    @Mock
    CategoryService categoryService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    BCryptPasswordEncoder encoder;
    @Mock
    UserServiceModelValidator validator;

    User user;
    UserServiceModel model;

    @Before
    public void initTests() {
        user = new User();
        model = new UserServiceModel();
        model.setUsername("name");
        model.setEmail("email");
        model.setPassword("1");
        model.setAuthorities(Set.of(new RoleServiceModel()));
        user.setUsername("pesho");

    }

    @Test(expected = UserWrongCredentialsException.class)
    public void registerUser_WhenNotValid_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(false);
        service.registerUser(model);
    }

    @Test
    public void registerUser_WhenAllOk_ShouldWork() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(modelMapper.map(model, User.class))
                .thenReturn(user);
        Mockito.when(modelMapper.map(user, UserServiceModel.class))
                .thenReturn(model);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);
        UserServiceModel result = service.registerUser(model);

        Mockito.verify(modelMapper).map(model, User.class);
        Mockito.verify(userRepository).saveAndFlush(user);
        Mockito.verify(modelMapper).map(user, UserServiceModel.class);

        assertEquals(model, result);
    }

    @Test
    public void loadUserByUsername_WhenUserExist_ShouldWork() {
        Mockito.when(userRepository.findByUsername("name"))
                .thenReturn(Optional.of(user));
        User userResult = (User) service.loadUserByUsername("name");
        assertEquals(user, userResult);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_WhenNotUserExist_ShouldThrow() {
        Mockito.when(userRepository.findByUsername("name"))
                .thenThrow(UsernameNotFoundException.class);
        service.loadUserByUsername("name");
    }

    @Test
    public void findByUsername_WhenUserExist_ShouldWork() {
        Mockito.when(userRepository.findByUsername("name"))
                .thenReturn(Optional.of(user));
        User userResult = (User) service.loadUserByUsername("name");
        assertEquals(user, userResult);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_WhenNotUserExist_ShouldThrow() {
        Mockito.when(userRepository.findByUsername("name"))
                .thenThrow(UsernameNotFoundException.class);
        service.loadUserByUsername("name");
    }

    @Test
    public void editUserProfile_WhenAllOk_ShouldWork() {
        user.setPassword("1");
        model.setUsername("name");
        Mockito.when(userRepository.findByUsername("name"))
                .thenReturn(Optional.of(user));
        Mockito.when(encoder.matches("1", "1"))
                .thenReturn(true);
        Mockito.when(encoder.encode("1"))
                .thenReturn("1");
        service.editUserProfile(model, "1");
        assertEquals(user.getEmail(), model.getEmail());
    }

    @Test(expected = UserNameTakenException.class)
    public void registerUser_WhenUserAlreadyExist_ShouldThrow() {
        Mockito.when(validator.isValid(model))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername("name"))
                .thenThrow(UserNameTakenException.class);
        service.registerUser(model);
    }

    @Test
    public void findAllUsers_WhenThereIsUsers_ShouldWork(){
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userRepository.findAll())
                .thenReturn(users);
        int result = service.findAllUsers().size();
        assertEquals(users.size(), result);
    }
}
