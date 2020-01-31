package softuni.project.services.validations;

import org.springframework.stereotype.Service;
import softuni.project.services.models.UserServiceModel;

@Service
public class UserServiceModelValidatorImpl implements UserServiceModelValidator {


    @Override
    public boolean isValid(UserServiceModel model) {
        return isUsernameValid(model.getUsername()) && isEmailValid(model.getEmail())
                && isPasswordValid(model.getPassword());
    }



    boolean isUsernameValid(String userName) {
        return userName != null;
    }

    boolean isPasswordValid(String password) {
        return password != null;
    }

    boolean isEmailValid(String email) {
        return email != null;
    }
}
