package softuni.project.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.GearBoxType;

@Getter
@Setter
@NoArgsConstructor
public class VehicleResponseModel extends BaseOfferResponseModel {

    private String brand;
    private String model;
    private String fuel;
    private GearBoxType gearBox;
    private Integer kilometers;
    private Boolean drivingLicenseNeeded;
    private String drivingCategory;
}
