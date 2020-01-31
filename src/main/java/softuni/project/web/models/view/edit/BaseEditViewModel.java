package softuni.project.web.models.view.edit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
abstract class BaseEditViewModel {

    private String id;
    private String image;
    private String name;
    private String description;
    private String startsOn;
    private String endsOn;
    private BigDecimal price;
    private String region;
    private String ownerPhoneNumber;
    private String categoryUrl;
}
