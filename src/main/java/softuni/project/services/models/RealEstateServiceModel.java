package softuni.project.services.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.Extras;
import softuni.project.data.entities.enums.TypeOfRealEstate;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RealEstateServiceModel extends BaseOfferServiceModel {

    private TypeOfRealEstate type;
    private Double area;
    private List<Extras> extras;
    private Boolean isPartyFree;
}
