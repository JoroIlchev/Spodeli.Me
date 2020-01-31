package softuni.project.validations.offers;

import org.springframework.validation.Errors;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.ClothesAddBindingModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static softuni.project.validations.offers.OfferValidationConstants.*;

@Validator
public class ClothesAddValidation implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ClothesAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ClothesAddBindingModel clothesAddBindingModel = (ClothesAddBindingModel) object;
        if (clothesAddBindingModel.getName() == null) {
            errors.rejectValue("name", (NAME_CAN_NOT_BE_EMPTY), (NAME_CAN_NOT_BE_EMPTY)
            );
        }
        if (clothesAddBindingModel.getDescription() == null) {
            errors.rejectValue("description", (DESCRIPTION_CAN_NOT_BE_EMPTY), (DESCRIPTION_CAN_NOT_BE_EMPTY)
            );
        }
        if (clothesAddBindingModel.getStartsOn() == null) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_EMPTY, STARTS_ON_CANT_BE_EMPTY
            );
        }
        if (clothesAddBindingModel.getEndsOn() == null) {
            errors.rejectValue("endsOn", ENDS_ON_CANT_BE_EMPTY, ENDS_ON_CANT_BE_EMPTY
            );
        }
        if (clothesAddBindingModel.getPrice() == null) {
            errors.rejectValue("price", PRICE_CANT_BE_EMPTY, PRICE_CANT_BE_EMPTY
            );
        }
        if (clothesAddBindingModel.getRegion() == null) {
            errors.rejectValue("region", (REGION_MUST_BE_CHOSEN), (REGION_MUST_BE_CHOSEN)
            );
        }
        if (clothesAddBindingModel.getOwnerPhoneNumber() == null) {
            errors.rejectValue("ownerPhoneNumber", PHONE_CANT_BE_EMPTY, PHONE_CANT_BE_EMPTY
            );
        }

        if (clothesAddBindingModel.getSize() == null) {
            errors.rejectValue("size", SIZE_CANT_BE_EMPTY, SIZE_CANT_BE_EMPTY
            );
        }
        if (clothesAddBindingModel.getEntityCondition() == null) {
            errors.rejectValue("entityCondition", CONDITION_CANT_BE_EMPTY, CONDITION_CANT_BE_EMPTY
            );
        }
        if (errors.hasErrors()) {
            return;
        }
        if (clothesAddBindingModel.getName().length() < NAME_MIN_SIZE ||
                clothesAddBindingModel.getName().length() > NAME_MAX_SIZE) {
            errors.rejectValue("name",
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE),
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE)
            );
        }
        if (clothesAddBindingModel.getStartsOn().isBefore(LocalDate.now())) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_YESTERDAY, STARTS_ON_CANT_BE_YESTERDAY
            );
        }
        if (clothesAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", PRICE_CANT_BE_NEGATIVE, PRICE_CANT_BE_NEGATIVE

            );
        }
        if (clothesAddBindingModel.getEndsOn().isBefore(clothesAddBindingModel.getStartsOn())) {
            errors.rejectValue("endsOn", ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON, ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON
            );
        }
    }
}
