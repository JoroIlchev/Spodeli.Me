package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TripAddBindingModel extends BaseOfferAddBindingModel {
    private String startPoint;
    private String endPoint;
    private String vehicleType;
    private Integer freeSeats;
    private Boolean suitcaseAllowed;
}
