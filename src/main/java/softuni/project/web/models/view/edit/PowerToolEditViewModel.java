package softuni.project.web.models.view.edit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerToolEditViewModel extends BaseEditViewModel {

    private String toolCondition;
    private Boolean isPortable;
    private Boolean isNeedExtraEquipment;
    private Boolean isNeedSpecialSkills;
    private Boolean depositNeeded;
}
