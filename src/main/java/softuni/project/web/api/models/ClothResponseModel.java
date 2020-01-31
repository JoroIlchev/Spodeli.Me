package softuni.project.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothResponseModel extends BaseOfferResponseModel{

    private String size;
    private String entityCondition;
    private Boolean depositNeeded;
}
