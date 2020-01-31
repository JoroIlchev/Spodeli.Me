package softuni.project.services.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesServiceModel extends BaseOfferServiceModel {
    private String size;
    private String entityCondition;
    private Boolean depositNeeded;
}
