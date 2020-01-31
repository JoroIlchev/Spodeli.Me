package softuni.project.services.interfaces;

import softuni.project.data.entities.Category;
import softuni.project.data.entities.User;

public interface InitService {
    User findByUsername(String username);
    Category findByCategoryName(String categoryName);

}
