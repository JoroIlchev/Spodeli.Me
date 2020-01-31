package softuni.project.services.interfaces;

import softuni.project.services.models.CategoryServiceModel;

import java.util.List;

public interface CategoryService {
//    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);


    CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel);
    CategoryServiceModel findById(String id);
    List<CategoryServiceModel> extractAllCategories();
    void seedCategories();
}
