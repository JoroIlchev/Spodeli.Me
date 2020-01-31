package softuni.unit.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import softuni.project.data.entities.enums.Region;
import softuni.project.services.models.VehicleServiceModel;
import softuni.project.services.validations.VehiclesServiceModelValidator;
import softuni.project.services.validations.VehiclesServiceModelValidatorImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


class VehiclesServiceModelValidatorTest {
    VehicleServiceModel model;
    @Mock
    private VehiclesServiceModelValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new VehiclesServiceModelValidatorImpl();
        model = new VehicleServiceModel();
        model.setName("Name");
        model.setDescription("Description");
        model.setStartsOn(LocalDate.now());
        model.setEndsOn(LocalDate.now());
        model.setPrice(BigDecimal.ZERO);
        model.setRegion(Region.Blagoevgrad);
        model.setOwnerPhoneNumber("0000");
        model.setBrand("Volvo");
        model.setModel("XC60");

    }

    @Test
    void isValid_WhenNameNull_ShouldFalse() {
        model.setName(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }

    @Test
    void isValid_WhenNameLessThanThreeSymbols_ShouldFalse() {
        model.setName("Ne");
        boolean result = validator.isValid(model);
        assertFalse(result);
    }
    @Test
    void isValid_WhenNameThreeSymbols_ShouldTrue() {
        model.setName("Yes");
        boolean result = validator.isValid(model);
        assertTrue(result);
    }
    @Test
    void isValid_WhenNameMoreThan20Symbols_ShouldFalse() {
        model.setName("111111111111111111111");
        boolean result = validator.isValid(model);
        assertFalse(result);
    }
    @Test
    void isValid_WhenName20Symbols_ShouldTrue() {
        model.setName("11111111111111111111");
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenDescriptionEmpty_ShouldFalse() {
        model.setDescription(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }
    @Test
    void isValid_WhenDescriptionNotEmpty_ShouldTrue() {
        model.setDescription("Nice");
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenStartsOnIsToday_ShouldTrue() {
        model.setStartsOn(LocalDate.now());
        boolean result = validator.isValid(model);
        assertTrue(result);
    }
    @Test
    void isValid_WhenStartsOnIsTomorrow_ShouldTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        model.setStartsOn(tomorrow);
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenStartsOnIsYesterday_ShouldFalse() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        model.setStartsOn(yesterday);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }

    @Test
    void isValid_WhenEndsOnIsToday_ShouldTrue() {
        model.setEndsOn(LocalDate.now());
        boolean result = validator.isValid(model);
        assertTrue(result);
    }
    @Test
    void isValid_WhenEndsOnIsTomorrow_ShouldTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        model.setEndsOn(tomorrow);
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenEndsOnIsYesterday_ShouldFalse() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        model.setEndsOn(yesterday);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }
    @Test
    void isValid_WhenPriceLessZero_ShouldFalse() {
        model.setPrice(BigDecimal.valueOf(-1));
        boolean result = validator.isValid(model);
        assertFalse(result);
    }

    @Test
    void isValid_WhenPriceZero_ShouldTrue() {
        model.setPrice(BigDecimal.valueOf(0));
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenRegionValid_ShouldTrue() {
        model.setRegion(Region.Blagoevgrad);
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenRegionNull_ShouldFalse() {
        model.setRegion(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }

    @Test
    void isValid_WhenPhoneNumberNull_ShouldFalse() {
        model.setOwnerPhoneNumber(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }
    @Test
    void isValid_WhenPhoneNumberValid_ShouldTrue() {
        model.setOwnerPhoneNumber("1");
        boolean result = validator.isValid(model);
        assertTrue(result);
    }

    @Test
    void isValid_WhenBrandNull_ShouldFalse() {
        model.setBrand(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }

    @Test
    void isValid_WhenModelNull_ShouldFalse() {
        model.setModel(null);
        boolean result = validator.isValid(model);
        assertFalse(result);
    }



}