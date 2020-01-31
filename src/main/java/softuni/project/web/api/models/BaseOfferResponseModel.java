package softuni.project.web.api.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.Region;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
abstract class BaseOfferResponseModel {
    private String id;
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
    private String user;
    private String categoryName;

}
