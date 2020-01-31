package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerToolsAddBindingModel extends BaseOfferAddBindingModel {

    private String toolCondition;
    private Boolean isPortable;
    private Boolean isNeedExtraEquipment;
    private Boolean isNeedSpecialSkills;
    private Boolean depositNeeded;
}
