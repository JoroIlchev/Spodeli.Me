package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerToolsViewModel extends BaseOfferViewModel {
    private String toolCondition;
    private String isPortable;
    private String isNeedExtraEquipment;
    private String isNeedSpecialSkills;
    private String depositNeeded;
}
