package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.GearBoxType;

@Getter
@Setter
@NoArgsConstructor
public class VehicleAddBindingModel extends BaseOfferAddBindingModel {

    private String brand;

    private String model;

    private String fuel;

    private GearBoxType gearBox;

    private Integer kilometers;

    private Boolean drivingLicenseNeeded;

    private String drivingCategory;
}
