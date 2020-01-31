package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Category;
import softuni.project.data.repositories.CategoryRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.services.models.CategoryServiceModel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.CATEGORY_NOT_FOUND;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryServiceModel findById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        return modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        category.setDescription(categoryServiceModel.getDescription());
        category.setName(categoryServiceModel.getName());
        if (categoryServiceModel.getImage() != null) {
            category.setImage(categoryServiceModel.getImage());
        }
        return modelMapper.map(categoryRepository.saveAndFlush(category), CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> extractAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryServiceModel.class))
                .sorted(Comparator.comparing(CategoryServiceModel::getName))
                .collect(Collectors.toList());

    }

    @Override
    public void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("Превозни средства",
                            "Търсиш коли, камиони, бусове, тротинетки, всичко което се движи?",
                            "http://res.cloudinary.com/joroilchev/image/upload/v1574517936/kqhmvn5zbwlwyeebpr6j.jpg",
                            "vehicles"),
                    new Category("Имоти",
                            "Търсиш квартира, апартамент, къща, вила, стая, гараж или помещение?.",
                            "http://res.cloudinary.com/joroilchev/image/upload/v1574428705/lb6eimfbcbvjqaswkilw.jpg",
                            "realestates"),
                    new Category("Машини и инструменти",
                            "Търсиш къртач, скеле, бормашина винтоверт или инструмент?",
                            "http://res.cloudinary.com/joroilchev/image/upload/v1574517775/u8brccmryrq0nyiyswxb.jpg",
                            "powertools"),
                    new Category("Дрехи и аксесоари",
                            "Търсиш костюм или рокля за специален повод? Или аксесоари?",
                            "http://res.cloudinary.com/joroilchev/image/upload/v1574672545/snb7tojvrgqwrvmqp2jr.jpg",
                            "clothes")
            );
            categoryRepository.saveAll(categories);
        }
    }
}
