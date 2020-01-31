package softuni.project.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.project.services.interfaces.CategoryService;
import softuni.project.services.interfaces.CloudinaryService;
import softuni.project.services.models.CategoryServiceModel;
import softuni.project.web.annotations.PageTitle;
import softuni.project.web.base.BaseController;
import softuni.project.web.models.binding.CategoryAddBindingModel;
import softuni.project.web.models.view.CategoryViewModel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Categories")
    public ModelAndView allCategory(ModelAndView modelAndView) {
        extractAllCategories(modelAndView);
        return view("category/all-category", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Category")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        CategoryViewModel categoryViewModel = modelMapper.map(categoryService.findById(id), CategoryViewModel.class);
        modelAndView.addObject("category", categoryViewModel);
        return view("category/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editConfirm(@ModelAttribute CategoryAddBindingModel model, @PathVariable String id) throws IOException {
        CategoryServiceModel categoryServiceModel = modelMapper.map(model, CategoryServiceModel.class);
        if (!model.getImage().isEmpty()) {
            categoryServiceModel.setImage(cloudinaryService.uploadImg(model.getImage()));
        } else {
            categoryServiceModel.setImage(null);
        }
        categoryService.editCategory(id, categoryServiceModel);
        return redirect("/category/all");
    }


    private void extractAllCategories(ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels = categoryService.extractAllCategories()
                .stream()
                .map(c -> modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("categories", categoryViewModels);
    }

}
