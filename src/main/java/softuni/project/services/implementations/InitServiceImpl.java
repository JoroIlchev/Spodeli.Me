package softuni.project.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Category;
import softuni.project.data.entities.User;
import softuni.project.data.repositories.CategoryRepository;
import softuni.project.data.repositories.UserRepository;
import softuni.project.exceptions.CategoryNotFoundException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.services.interfaces.InitService;

import static softuni.project.services.ServiceConstants.CATEGORY_WITH_NAME_NOT_FOUND;
import static softuni.project.services.ServiceConstants.USER_NOT_FOUND;

@Service
public class InitServiceImpl implements InitService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public InitServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    @Override
    public Category findByCategoryName(String categoryName) {

        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(String.format(CATEGORY_WITH_NAME_NOT_FOUND, categoryName)));
    }
}
