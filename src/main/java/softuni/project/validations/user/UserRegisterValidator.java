package softuni.project.validations.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import softuni.project.data.repositories.UserRepository;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.UserRegisterBindingModel;

@Validator
public class UserRegisterValidator implements org.springframework.validation.Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) o;

        if (userRegisterBindingModel.getUsername() == null ||
                userRegisterBindingModel.getUsername().isEmpty()) {
            errors.rejectValue(
                    "username",
                    ValidationConstants.USERNAME_CAN_NOT_BE_EMPTY,
                    ValidationConstants.USERNAME_CAN_NOT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getPassword() == null ||
                userRegisterBindingModel.getPassword().isEmpty()) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORD_CANT_BE_EMPTY,
                    ValidationConstants.PASSWORD_CANT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getEmail() == null ||
                userRegisterBindingModel.getEmail().isEmpty()) {
            errors.rejectValue(
                    "email",
                    ValidationConstants.EMAIL_CANT_BE_EMPTY,
                    ValidationConstants.EMAIL_CANT_BE_EMPTY
            );
        }
        if (errors.hasErrors()){
            return;
        }


        if (this.userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername()),
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername())
            );
        }

        if (userRegisterBindingModel.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH ||
                userRegisterBindingModel.getUsername().length() > ValidationConstants.USERNAME_MAX_LENGTH) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_LENGTH, ValidationConstants.USERNAME_MIN_LENGTH,
                            ValidationConstants.USERNAME_MAX_LENGTH),
                    String.format(ValidationConstants.USERNAME_LENGTH, ValidationConstants.USERNAME_MIN_LENGTH,
                            ValidationConstants.USERNAME_MAX_LENGTH)
            );
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }

        if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())
            );
        }
    }
}
