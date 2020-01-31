package softuni.unit.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import softuni.project.data.entities.enums.Region;
import softuni.project.services.models.ClothesServiceModel;
import softuni.project.services.validations.ClothesServiceModelValidator;
import softuni.project.services.validations.ClothesServiceModelValidatorImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


class ClothesServiceModelValidatorTest  {
    ClothesServiceModel clothes;
    @Mock
    private ClothesServiceModelValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new ClothesServiceModelValidatorImpl();
        clothes = new ClothesServiceModel();
        clothes.setName("Name");
        clothes.setDescription("Description");
        clothes.setStartsOn(LocalDate.now());
        clothes.setEndsOn(LocalDate.now());
        clothes.setPrice(BigDecimal.ZERO);
        clothes.setRegion(Region.Blagoevgrad);
        clothes.setOwnerPhoneNumber("0000");
        clothes.setSize("XXL");
        clothes.setEntityCondition("New");
    }

    @Test
    void isValid_WhenNameNull_ShouldFalse() {
        clothes.setName(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenNameLessThanThreeSymbols_ShouldFalse() {
        clothes.setName("Ne");
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }
    @Test
    void isValid_WhenNameThreeSymbols_ShouldTrue() {
        clothes.setName("Yes");
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }
    @Test
    void isValid_WhenNameMoreThan20Symbols_ShouldFalse() {
        clothes.setName("111111111111111111111");
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }
    @Test
    void isValid_WhenName20Symbols_ShouldTrue() {
        clothes.setName("11111111111111111111");
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenDescriptionEmpty_ShouldFalse() {
        clothes.setDescription(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }
    @Test
    void isValid_WhenDescriptionNotEmpty_ShouldTrue() {
        clothes.setDescription("Nice");
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenStartsOnIsToday_ShouldTrue() {
        clothes.setStartsOn(LocalDate.now());
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }
    @Test
    void isValid_WhenStartsOnIsTomorrow_ShouldTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        clothes.setStartsOn(tomorrow);
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenStartsOnIsYesterday_ShouldFalse() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        clothes.setStartsOn(yesterday);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenEndsOnIsToday_ShouldTrue() {
        clothes.setEndsOn(LocalDate.now());
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }
    @Test
    void isValid_WhenEndsOnIsTomorrow_ShouldTrue() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        clothes.setEndsOn(tomorrow);
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenEndsOnIsYesterday_ShouldFalse() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        clothes.setEndsOn(yesterday);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }
    @Test
    void isValid_WhenPriceLessZero_ShouldFalse() {
        clothes.setPrice(BigDecimal.valueOf(-1));
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenPriceZero_ShouldTrue() {
        clothes.setPrice(BigDecimal.valueOf(0));
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenRegionValid_ShouldTrue() {
        clothes.setRegion(Region.Blagoevgrad);
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenRegionNull_ShouldFalse() {
        clothes.setRegion(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenPhoneNumberNull_ShouldFalse() {
        clothes.setOwnerPhoneNumber(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }
    @Test
    void isValid_WhenPhoneNumberValid_ShouldTrue() {
        clothes.setOwnerPhoneNumber("1");
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenSizeNull_ShouldFalse() {
        clothes.setSize(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenSizeValid_ShouldTrue() {
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }

    @Test
    void isValid_WhenConditionNull_ShouldFalse() {
        clothes.setEntityCondition(null);
        boolean result = validator.isValid(clothes);
        assertFalse(result);
    }

    @Test
    void isValid_WhenConditionValid_ShouldTrue() {
        boolean result = validator.isValid(clothes);
        assertTrue(result);
    }


}