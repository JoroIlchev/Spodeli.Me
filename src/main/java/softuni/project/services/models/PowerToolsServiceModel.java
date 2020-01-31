package softuni.project.services.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerToolsServiceModel extends BaseOfferServiceModel {

    private String toolCondition;
    private Boolean isPortable;
    private Boolean isNeedExtraEquipment;
    private Boolean isNeedSpecialSkills;
    private Boolean depositNeeded;
}
