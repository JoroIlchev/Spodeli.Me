package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.view.CategoryViewModel;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {

    private CategoryService categoryService;
    private ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView index(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("index", modelAndView);
    }

    @GetMapping("/home")
    @PageTitle("Home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("home", modelAndView);
    }

    @GetMapping("/index")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView indexAfterLogout(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("index", modelAndView);
    }

    private void extractAllCategories(ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels = categoryService.extractAllCategories()
                .stream()
                .map(c -> modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("categories", categoryViewModels);
    }


}
