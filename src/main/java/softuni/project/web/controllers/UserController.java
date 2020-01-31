package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.services.interfaces.UserService;
import softuni.project.services.models.RoleServiceModel;
import softuni.project.services.models.UserServiceModel;
import softuni.project.validations.user.UserEditValidator;
import softuni.project.validations.user.UserRegisterValidator;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.UserEditBindingModel;
import softuni.project.web.models.binding.UserRegisterBindingModel;
import softuni.project.web.models.view.UserViewModel;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")

public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserEditValidator userEditValidator;
    private final UserRegisterValidator userRegisterValidator;
    private final CategoryService categoryService;


    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserEditValidator userEditValidator,
                          UserRegisterValidator userRegisterValidator, CategoryService categoryService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userEditValidator = userEditValidator;
        this.userRegisterValidator = userRegisterValidator;
        this.categoryService = categoryService;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Registration")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model) {
        modelAndView.addObject("model", model);

        return view("register", modelAndView);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel model,
                                        BindingResult bindingResult) {
        userRegisterValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);

            return view("register", modelAndView);
        }

        UserServiceModel userServiceModel = modelMapper.map(model, UserServiceModel.class);
        userService.registerUser(userServiceModel);

        return redirect("/users/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login(ModelAndView modelAndView) {
        return view("login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView allUsers(ModelAndView modelAndView, Principal principal) {
        String username = getUsername(principal);

        List<UserViewModel> users = getAllUsersWithoutRoot(username);

        modelAndView.addObject("users", users);
        return view("users/all", modelAndView);
    }

    private List<UserViewModel> getAllUsersWithoutRoot(String username) {
        List<UserViewModel> userViewModels = userService.findAllUsers().stream()
                .map(u -> {
                    UserViewModel user = this.modelMapper.map(u, UserViewModel.class);
                    user.setAuthorities(u.getAuthorities().stream()
                            .map(a -> a.getAuthority().substring(5)).collect(Collectors.toSet()));
                    return user;
                })
                .filter(us -> !us.getAuthorities().contains("ROOT"))
                .collect(Collectors.toList());

        UserServiceModel userServiceModel = userService.findByUsername(username);
        UserViewModel userViewModel = modelMapper.map(userServiceModel, UserViewModel.class);
        userViewModel.setAuthorities(userServiceModel.getAuthorities().stream()
                .map(RoleServiceModel::getAuthority).collect(Collectors.toSet()));

        if (userViewModel.getAuthorities().contains("ROLE_ROOT")){
            return userViewModels;
        }

        if (userViewModel.getAuthorities().contains("ROLE_ADMIN")) {
            String finalUserRole = "ADMIN";
            return userViewModels.stream()
                    .filter(u -> !u.getAuthorities().contains(finalUserRole))
                    .collect(Collectors.toList());
        }

        return userViewModels;
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserRole(id, "user");
        return redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        userService.setUserRole(id, "moderator");
        return redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserRole(id, "admin");
        return redirect("/users/all");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Profile")
    public ModelAndView myProfile(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = userService.findByUsername(principal.getName());
        UserViewModel model = modelMapper.map(userServiceModel, UserViewModel.class);
        modelAndView.addObject("model", model);
        return view("users/my-profile", modelAndView);

    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit My Profile")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditBindingModel model) {
        UserServiceModel userServiceModel = this.userService.findByUsername(principal.getName());
        model = modelMapper.map(userServiceModel, UserEditBindingModel.class);
        model.setPassword(null);
        modelAndView.addObject("model", model);
        return view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(ModelAndView modelAndView,
                                           @ModelAttribute(name = "model") UserEditBindingModel model,
                                           BindingResult bindingResult) {
        userEditValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setOldPassword(null);
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);
            return view("users/edit-profile", modelAndView);
        }
        UserServiceModel userServiceModel = this.modelMapper.map(model, UserServiceModel.class);
        this.userService.editUserProfile(userServiceModel, model.getOldPassword());

        return redirect("/users/profile");
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}

