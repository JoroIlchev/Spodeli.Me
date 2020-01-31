package softuni.project.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.GearBoxType;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseOffer{

    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "fuel_type")
    private String fuel;
    @Enumerated(EnumType.STRING)
    @Column(name = "gear_box_type")
    private GearBoxType gearBox;
    @Column(name = "kilometers")
    private Integer kilometers;
    @Column(name = "driving_license_needed")
    private Boolean drivingLicenseNeeded;
    @Column(name = "driving_category")
    private String drivingCategory;


}
