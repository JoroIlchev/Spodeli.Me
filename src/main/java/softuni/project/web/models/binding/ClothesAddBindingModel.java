package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothesAddBindingModel extends BaseOfferAddBindingModel {


    private String size;
    private String entityCondition;
    private Boolean depositNeeded;
}
