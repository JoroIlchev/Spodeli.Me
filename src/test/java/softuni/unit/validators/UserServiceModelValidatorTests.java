package softuni.unit.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import softuni.project.services.models.UserServiceModel;
import softuni.project.services.validations.UserServiceModelValidator;
import softuni.project.services.validations.UserServiceModelValidatorImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UserServiceModelValidatorTests {
    UserServiceModel user;
    @Mock
    private UserServiceModelValidator validator;

    @BeforeEach
    public void setUp() {
        user = new UserServiceModel();
        validator = new UserServiceModelValidatorImpl();
        user.setUsername("Pesho");
        user.setPassword("11");
        user.setEmail("mail");
    }

    @Test
    void isValid_WhenNameNull_ShouldFalse() {
        user.setUsername(null);
        boolean result = validator.isValid(user);
        assertFalse(result);
    }
    @Test
    void isValid_WhenNameValid_ShouldTrue() {
        boolean result = validator.isValid(user);
        assertTrue(result);
    }

    @Test
    void isValid_WhenPasswordNull_ShouldFalse() {
        user.setPassword(null);
        boolean result = validator.isValid(user);
        assertFalse(result);
    }
    @Test
    void isValid_WhenPassWOrdValid_ShouldTrue() {
        boolean result = validator.isValid(user);
        assertTrue(result);
    }

    @Test
    void isValid_WhenEmailNull_ShouldFalse() {
        user.setEmail(null);
        boolean result = validator.isValid(user);
        assertFalse(result);
    }
    @Test
    void isValid_WhenEmailValid_ShouldTrue() {
        boolean result = validator.isValid(user);
        assertTrue(result);
    }

    @Test
    void isValid_WhenAllOk_ShouldTrue(){
        boolean result = validator.isValid(user);
        assertTrue(result);
    }


}
