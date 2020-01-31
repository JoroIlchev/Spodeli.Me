package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleViewModel extends BaseOfferViewModel {
    private String brand;

    private String model;

    private String fuel;

    private String gearBox;

    private Integer kilometers;

    private String drivingLicenseNeeded;

    private String drivingCategory;


}
