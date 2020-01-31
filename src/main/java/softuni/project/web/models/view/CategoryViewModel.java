package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryViewModel {
    private String id;
    private String name;
    private String description;
    private String image;
    private String categoryUrl;
}
