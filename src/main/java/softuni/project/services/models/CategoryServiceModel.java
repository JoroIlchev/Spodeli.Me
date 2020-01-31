package softuni.project.services.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryServiceModel extends BaseServiceModel{

    private String name;
    private String description;
    private String image;
    private String categoryUrl;

}
