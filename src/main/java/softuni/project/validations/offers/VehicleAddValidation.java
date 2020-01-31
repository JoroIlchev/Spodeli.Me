package softuni.project.validations.offers;

import org.springframework.validation.Errors;
import softuni.project.validations.annotation.Validator;
import softuni.project.web.models.binding.VehicleAddBindingModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static softuni.project.validations.offers.OfferValidationConstants.*;

@Validator
public class VehicleAddValidation implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return VehicleAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        VehicleAddBindingModel vehicleAddBindingModel = (VehicleAddBindingModel) o;

        if (vehicleAddBindingModel.getName() == null) {
            errors.rejectValue(
                    "name", (NAME_CAN_NOT_BE_EMPTY), (NAME_CAN_NOT_BE_EMPTY)
            );
        }
        if (vehicleAddBindingModel.getDescription() == null) {
            errors.rejectValue(
                    "description", (DESCRIPTION_CAN_NOT_BE_EMPTY), (DESCRIPTION_CAN_NOT_BE_EMPTY)
            );
        }
        if (vehicleAddBindingModel.getStartsOn() == null) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_EMPTY, STARTS_ON_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getEndsOn() == null) {
            errors.rejectValue(
                    "endsOn", ENDS_ON_CANT_BE_EMPTY, ENDS_ON_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getPrice() == null) {
            errors.rejectValue("price", PRICE_CANT_BE_EMPTY, PRICE_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getOwnerPhoneNumber() == null) {
            errors.rejectValue("ownerPhoneNumber", PHONE_CANT_BE_EMPTY, PHONE_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getBrand() == null) {
            errors.rejectValue("type", BRAND_CANT_BE_EMPTY, BRAND_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getModel() == null) {
            errors.rejectValue("type", MODEL_CANT_BE_EMPTY, MODEL_CANT_BE_EMPTY);
        }
        if (vehicleAddBindingModel.getRegion() == null) {
            errors.rejectValue("region", (REGION_MUST_BE_CHOSEN), (REGION_MUST_BE_CHOSEN)
            );
        }

        if (errors.hasErrors()) {
            return;
        }
        if (vehicleAddBindingModel.getName().length() < NAME_MIN_SIZE ||
                vehicleAddBindingModel.getName().length() > NAME_MAX_SIZE) {
            errors.rejectValue(
                    "name",
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE),
                    String.format(OfferValidationConstants.NAME_SIZE_ERROR, NAME_MIN_SIZE, NAME_MAX_SIZE)
            );
        }
        if (vehicleAddBindingModel.getStartsOn().isBefore(LocalDate.now())) {
            errors.rejectValue("startsOn", STARTS_ON_CANT_BE_YESTERDAY, STARTS_ON_CANT_BE_YESTERDAY);
        }
        if (vehicleAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", PRICE_CANT_BE_NEGATIVE, PRICE_CANT_BE_NEGATIVE);
        }
        if (vehicleAddBindingModel.getEndsOn().isBefore(vehicleAddBindingModel.getStartsOn())) {
            errors.rejectValue(
                    "endsOn",
                    ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON,
                    ENDS_ON_CANT_BE_OLDER_THAN_STARTS_ON
            );
        }


    }
}
