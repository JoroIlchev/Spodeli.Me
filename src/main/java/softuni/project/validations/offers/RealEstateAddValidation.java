package softuni.project.validations.offers;

import org.springframework.validation.Errors;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.RealEstateAddBindingModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static softuni.project.validations.offers.OfferValidationConstants.*;
import static softuni.project.validations.offers.OfferValidationConstants.PHONE_CANT_BE_EMPTY;

@Validator
public class RealEstateAddValidation implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return RealEstateAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RealEstateAddBindingModel realEstateAddBindingModel = (RealEstateAddBindingModel) o;

        if (realEstateAddBindingModel.getName() == null) {
            errors.rejectValue("name", (NAME_CAN_NOT_BE_EMPTY), (NAME_CAN_NOT_BE_EMPTY)
            );
        }
        if (realEstateAddBindingModel.getDescription() == null) {
            errors.rejectValue("description", (DESCRIPTION_CAN_NOT_BE_EMPTY), (DESCRIPTION_CAN_NOT_BE_EMPTY)
            );
        }
        if (realEstateAddBindingModel.getStartsOn() == null) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_EMPTY, STARTS_ON_CANT_BE_EMPTY
            );
        }
        if (realEstateAddBindingModel.getEndsOn() == null) {
            errors.rejectValue("endsOn", ENDS_ON_CANT_BE_EMPTY, ENDS_ON_CANT_BE_EMPTY
            );
        }
        if (realEstateAddBindingModel.getPrice() == null) {
            errors.rejectValue("price", PRICE_CANT_BE_EMPTY, PRICE_CANT_BE_EMPTY

            );
        }
        if (realEstateAddBindingModel.getRegion() == null) {
            errors.rejectValue("region", (REGION_MUST_BE_CHOSEN), (REGION_MUST_BE_CHOSEN)
            );
        }
        if (realEstateAddBindingModel.getOwnerPhoneNumber() == null) {
            errors.rejectValue("ownerPhoneNumber", PHONE_CANT_BE_EMPTY, PHONE_CANT_BE_EMPTY

            );
        }
        if (realEstateAddBindingModel.getType() == null) {
            errors.rejectValue("type", REAL_ESTATE_TYPE_CANT_BE_EMPTY, REAL_ESTATE_TYPE_CANT_BE_EMPTY

            );
        }
        if (errors.hasErrors()) {
            return;
        }
        if (realEstateAddBindingModel.getName().length() < NAME_MIN_SIZE ||
                realEstateAddBindingModel.getName().length() > NAME_MAX_SIZE) {
            errors.rejectValue(
                    "name",
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE),
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE)
            );
        }
        if (realEstateAddBindingModel.getStartsOn().isBefore(LocalDate.now())) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_YESTERDAY, STARTS_ON_CANT_BE_YESTERDAY
            );
        }
        if (realEstateAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", PRICE_CANT_BE_NEGATIVE, PRICE_CANT_BE_NEGATIVE

            );
        }
        if (realEstateAddBindingModel.getEndsOn().isBefore(realEstateAddBindingModel.getStartsOn())) {
            errors.rejectValue("endsOn", ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON,
                    ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON
            );
        }


    }
}
