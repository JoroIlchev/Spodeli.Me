package softuni.project.validations.offers;

import org.springframework.validation.Errors;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.PowerToolsAddBindingModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static softuni.project.validations.offers.OfferValidationConstants.*;
import static softuni.project.validations.offers.OfferValidationConstants.PHONE_CANT_BE_EMPTY;

@Validator
public class PowerToolsAddValidation implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PowerToolsAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PowerToolsAddBindingModel powerToolsAddBindingModel = (PowerToolsAddBindingModel) o;

        if (powerToolsAddBindingModel.getName() == null) {
            errors.rejectValue(
                    "name",
                    (NAME_CAN_NOT_BE_EMPTY),
                    (NAME_CAN_NOT_BE_EMPTY)
            );
        }
        if (powerToolsAddBindingModel.getDescription() == null) {
            errors.rejectValue(
                    "description",
                    (DESCRIPTION_CAN_NOT_BE_EMPTY),
                    (DESCRIPTION_CAN_NOT_BE_EMPTY)
            );
        }

        if (powerToolsAddBindingModel.getStartsOn() == null) {
            errors.rejectValue(
                    "startsOn",
                    STARTS_ON_CANT_BE_EMPTY,
                    STARTS_ON_CANT_BE_EMPTY
            );
        }
        if (powerToolsAddBindingModel.getEndsOn() == null) {
            errors.rejectValue(
                    "endsOn",
                    ENDS_ON_CANT_BE_EMPTY,
                    ENDS_ON_CANT_BE_EMPTY
            );
        }
        if (powerToolsAddBindingModel.getPrice() == null) {
            errors.rejectValue(
                    "price",
                    PRICE_CANT_BE_EMPTY,
                    PRICE_CANT_BE_EMPTY

            );
        }
        if (powerToolsAddBindingModel.getRegion() == null) {
            errors.rejectValue(
                    "region",
                    (REGION_MUST_BE_CHOSEN),
                    (REGION_MUST_BE_CHOSEN)
            );
        }
        if (powerToolsAddBindingModel.getOwnerPhoneNumber() == null) {
            errors.rejectValue(
                    "ownerPhoneNumber",
                    PHONE_CANT_BE_EMPTY,
                    PHONE_CANT_BE_EMPTY

            );
        }
        if (powerToolsAddBindingModel.getToolCondition() == null) {
            errors.rejectValue(
                    "toolCondition",
                    TOOL_CONDITION,
                    TOOL_CONDITION
            );
        }

        if (errors.hasErrors()) {
            return;
        }
        if (powerToolsAddBindingModel.getName().length() < NAME_MIN_SIZE ||
                powerToolsAddBindingModel.getName().length() > NAME_MAX_SIZE) {
            errors.rejectValue(
                    "name",
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE),
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE)
            );
        }
        if (powerToolsAddBindingModel.getStartsOn().isBefore(LocalDate.now())) {
            errors.rejectValue(
                    "startsOn",
                    STARTS_ON_CANT_BE_YESTERDAY,
                    STARTS_ON_CANT_BE_YESTERDAY
            );
        }
        if (powerToolsAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue(
                    "price",
                    PRICE_CANT_BE_NEGATIVE,
                    PRICE_CANT_BE_NEGATIVE

            );
        }
        if (powerToolsAddBindingModel.getEndsOn().isBefore(powerToolsAddBindingModel.getStartsOn())) {
            errors.rejectValue(
                    "endsOn",
                    ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON,
                    ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON
            );
        }

    }
}
