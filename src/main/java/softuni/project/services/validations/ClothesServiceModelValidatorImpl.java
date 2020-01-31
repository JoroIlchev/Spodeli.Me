package softuni.project.services.validations;

import org.springframework.stereotype.Service;
import softuni.project.data.entities.enums.Region;
import softuni.project.services.models.ClothesServiceModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static softuni.project.validations.offers.OfferValidationConstants.NAME_MAX_SIZE;
import static softuni.project.validations.offers.OfferValidationConstants.NAME_MIN_SIZE;

@Service
public class ClothesServiceModelValidatorImpl implements ClothesServiceModelValidator {


    @Override
    public boolean isValid(ClothesServiceModel model) {
        return isNameValid(model.getName()) && isDescriptionValid(model.getDescription())
                && isStartOnValid(model.getStartsOn()) && isEndsOnValid(model.getEndsOn())
                && isPriceValid(model.getPrice()) && isRegionValid(model.getRegion())
                && isOwnerPhoneNumberValid(model.getOwnerPhoneNumber()) && isSizeValid(model.getSize())
                && isEntityCondition(model.getEntityCondition());
    }

    boolean isNameValid(String name) {
        return name != null && name.length() >= NAME_MIN_SIZE && name.length() <= NAME_MAX_SIZE;
    }

    boolean isDescriptionValid(String description) {
        return description != null;
    }

    boolean isStartOnValid(LocalDate startsOn) {
        return startsOn.isAfter(LocalDate.now()) || startsOn.isEqual(LocalDate.now());
    }

    boolean isEndsOnValid(LocalDate endsOn) {
        return endsOn.isAfter(LocalDate.now()) || endsOn.isEqual(LocalDate.now());
    }

    boolean isPriceValid(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) >= 0;
    }

    boolean isRegionValid(Region region) {
        if (region == null){
            return false;
        }
        for (Region value : Region.values()) {
            if (value.getName().equals(region.getName())) {
                return true;
            }
        }
        return false;
    }

    boolean isOwnerPhoneNumberValid(String ownerPhoneNumber) {
        return ownerPhoneNumber != null;
    }

    boolean isSizeValid(String size) {
        return size != null;
    }

    boolean isEntityCondition(String entityCondition) {
        return entityCondition != null;
    }
}
