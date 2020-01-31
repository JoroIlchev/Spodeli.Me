package softuni.unit.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import softuni.project.data.entities.Category;
import softuni.project.data.entities.User;
import softuni.project.data.repositories.CategoryRepository;
import softuni.project.data.repositories.UserRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.implementations.InitServiceImpl;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class InitServiceTest {

    @InjectMocks
    InitServiceImpl initService;
    @Mock
    UserRepository userRepository;
    @Mock
    CategoryRepository categoryRepository;


    @Test
    public void findByUsername_WhenUserExist_ShouldReturnUser() {
        Optional<User> optionalUser = Optional.of(new User());
        Mockito.when(userRepository.findByUsername("name")).thenReturn((optionalUser));
        Optional<User> resultUser = userRepository.findByUsername("name");
        Mockito.verify(userRepository).findByUsername("name");
        assertEquals(optionalUser, resultUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void findByUsername_WhenNoUser_ShouldThrow() {
        Mockito.when(userRepository.findByUsername("new"))
                .thenReturn(Optional.empty());
        initService.findByUsername("new");

    }

    @Test(expected = IllegalArgumentException.class)
    public void findByUsername_WhenUserNull_ShouldThrow() {
        Mockito.when(userRepository.findByUsername(null))
                .thenThrow(new IllegalArgumentException());
        initService.findByUsername(null);

    }

    @Test(expected = CategoryNotFoundException.class)
    public void findCategoryByName_invalidName_throwsCategoryNotFoundException() {
        Mockito.when(categoryRepository.findByName("name"))
                .thenReturn(Optional.empty());
        initService.findByCategoryName("name");
    }

    @Test(expected = NullPointerException.class)
    public void findCategoryByName_whenNameNULL_throwsNullPointerException() {
        Mockito.when(categoryRepository.findByName(null))
                .thenThrow(new NullPointerException());
        initService.findByCategoryName(null);
    }

    @Test
    public void findCategoryByName_whenNameOk_ShouldWork() {
        Optional<Category> category = Optional.of(new Category());
        Mockito.when(categoryRepository.findByName("name"))
                .thenReturn(category);
        Optional<Category> result = categoryRepository.findByName("name");
        assertEquals(category, result);
    }
}
