package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RealEstateViewModel extends BaseOfferViewModel {

    private String type;

    private Double area;

    private String extras;

    private String isPartyFree;
}
