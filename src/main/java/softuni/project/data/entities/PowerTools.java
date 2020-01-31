package softuni.project.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "power_tools")
public class PowerTools extends BaseOffer {

    @Column(name = "tool_Condition", nullable = false)
    private String toolCondition;

    @Column(name = "is_portable")
    private Boolean isPortable;

    @Column(name = "need_extra_equipment")
    private Boolean isNeedExtraEquipment;

    @Column(name = "need_special_skills")
    private Boolean isNeedSpecialSkills;

    @Column(name = "deposit_needed")
    private Boolean depositNeeded;


}
