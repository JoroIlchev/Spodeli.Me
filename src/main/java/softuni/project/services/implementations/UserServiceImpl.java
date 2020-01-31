package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.User;
import softuni.project.data.repositories.UserRepository;
import softuni.project.exceptions.UserNameTakenException;
import softuni.project.exceptions.UserNotFoundException;
import softuni.project.exceptions.UserWrongCredentialsException;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.services.interfaces.RoleService;
import softuni.project.services.interfaces.UserService;
import softuni.project.services.models.UserServiceModel;
import softuni.project.services.validations.UserServiceModelValidator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.project.services.ServiceConstants.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    private static final String INCORRECT_PASSWORD = "Incorrect password";
    private static final String INCORRECT_ID = "Incorrect id";
    private static final String USERNAME_IS_NOT_FREE = "User name is already taken!";
    private static final String USERNAME_BAD_CREDENTIALS = "Account was not created, bad credentials!";

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;
    private final UserServiceModelValidator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, CategoryService categoryService,
                           ModelMapper modelMapper, BCryptPasswordEncoder encoder, UserServiceModelValidator validator) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.validator = validator;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        if (!validator.isValid(userServiceModel)){
            throw new UserWrongCredentialsException(USERNAME_BAD_CREDENTIALS);
        }

        if (userRepository.findByUsername(userServiceModel.getUsername()).isPresent()){
            throw new UserNameTakenException(USERNAME_IS_NOT_FREE);
        }
        if (userRepository.count() == 0) {
            roleService.seedRoles();
            categoryService.seedCategories();
            userServiceModel.setAuthorities(roleService.findAll());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
        }

        User user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    @Override
    public UserServiceModel findByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));

        return modelMapper.map(user, UserServiceModel.class);
    }


    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, userServiceModel.getUsername())));
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new UserNotFoundException(INCORRECT_PASSWORD);
        }
        user.setPassword(userServiceModel.getPassword() != null ? encoder.encode(userServiceModel.getPassword()) :
                user.getPassword());
        user.setEmail(userServiceModel.getEmail());
        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {

        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void setUserRole(String id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(INCORRECT_ID));
        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();
        switch (role) {
            case "user":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                break;
            case "moderator":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }
        userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));
    }
}