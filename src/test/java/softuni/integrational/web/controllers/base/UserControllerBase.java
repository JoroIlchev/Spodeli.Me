package softuni.integrational.web.controllers.base;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.project.data.repositories.UserRepository;
import softuni.project.validations.user.UserRegisterValidator;


public class UserControllerBase extends WebBaseTest {
    public final static String REGISTER_VIEW = "register";
    public final static String LOGIN_REDIRECT = "http://localhost/users/login";
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected ModelMapper modelMapper;
    @MockBean
    protected UserRegisterValidator validator;

}
