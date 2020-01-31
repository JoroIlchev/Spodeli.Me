package softuni.project.validations.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import softuni.project.data.entities.User;
import softuni.project.data.repositories.UserRepository;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.UserEditBindingModel;

import static softuni.project.validations.user.ValidationConstants.*;

@Validator
public class UserEditValidator implements org.springframework.validation.Validator {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserEditValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEditBindingModel userEditBindingModel = (UserEditBindingModel) o;

        User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElse(null);

        if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue(
                    "oldPassword",
                    WRONG_PASSWORD,
                    WRONG_PASSWORD
            );
        }

        if (userEditBindingModel.getPassword() != null && !userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    PASSWORDS_DO_NOT_MATCH,
                    PASSWORDS_DO_NOT_MATCH
            );
        }

        if (!user.getEmail().equals(userEditBindingModel.getEmail()) && this.userRepository.findByEmail(userEditBindingModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail())
            );
        }

    }
}

