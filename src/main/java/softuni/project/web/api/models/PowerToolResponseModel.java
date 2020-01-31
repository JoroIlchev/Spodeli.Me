package softuni.project.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerToolResponseModel extends BaseOfferResponseModel {
    private String toolCondition;
    private Boolean isPortable;
    private Boolean isNeedExtraEquipment;
    private Boolean isNeedSpecialSkills;
    private Boolean depositNeeded;
}
