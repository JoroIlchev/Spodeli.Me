package softuni.project.web.models.view.edit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.GearBoxType;

@Getter
@Setter
@NoArgsConstructor
public class VehiclesEditViewModel extends BaseEditViewModel {
    private String brand;
    private String model;
    private String fuel;
    private GearBoxType gearBox;
    private Integer kilometers;
    private Boolean drivingLicenseNeeded;
    private String drivingCategory;
}
