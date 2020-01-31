package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
abstract class BaseOfferViewModel {


    private String image;
    private String name;
    private String description;
    private String startsOn;
    private String endsOn;
    @NumberFormat(pattern = "##0.00")
    private BigDecimal price;
    private String region;
    private String ownerPhoneNumber;

}
