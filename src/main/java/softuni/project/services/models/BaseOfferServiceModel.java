package softuni.project.services.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.Region;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
abstract class BaseOfferServiceModel extends BaseServiceModel {

    private String name;

    private String description;

    private String image;

    private LocalDate startsOn;

    private LocalDate endsOn;

    private BigDecimal price;

    private Region region;

    private Boolean isActive;

    private Boolean isApproved;

    private String ownerPhoneNumber;

    private UserServiceModel user;

    private CategoryServiceModel categoryServiceModel;

    private Date timeStamp;
}
