package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothesViewModel extends BaseOfferViewModel {
    private String size;
    private String entityCondition;
    private String depositNeeded;
}
