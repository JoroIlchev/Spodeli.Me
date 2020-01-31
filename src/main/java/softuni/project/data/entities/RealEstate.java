package softuni.project.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.project.data.entities.enums.Extras;
import softuni.project.data.entities.enums.TypeOfRealEstate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "real_estates")
public class RealEstate extends BaseOffer {

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private TypeOfRealEstate type;
    @Column(name = "area")
    private Double area;
    @ElementCollection(targetClass = Extras.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "real_estate_extras")
    private List<Extras> extras;
    @Column(name = "is_party_free")
    private Boolean isPartyFree;




}
