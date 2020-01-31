package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentAddBindingModel extends BaseOfferAddBindingModel {

    private Boolean isPortable;

    private Boolean isNeedSpecialSkills;

    private Boolean depositNeeded;
}
