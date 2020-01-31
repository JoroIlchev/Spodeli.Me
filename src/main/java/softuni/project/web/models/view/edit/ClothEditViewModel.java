package softuni.project.web.models.view.edit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothEditViewModel extends BaseEditViewModel {

    private String size;
    private String entityCondition;
    private Boolean depositNeeded;
}
