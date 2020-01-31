package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CategoryAddBindingModel {
    private String name;
    private String description;
    private MultipartFile image;
    private String categoryUrl;
}
