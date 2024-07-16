package gift.category.api;

import gift.category.application.CategoryService;
import gift.category.dto.CategoryRequest;
import gift.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable("id") Long id) {
        return categoryService.getCategoryByIdOrThrow(id);
    }

    @PostMapping
    public CategoryResponse addCategory(@RequestBody @Valid CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @DeleteMapping("/{id}")
    public Long deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategoryById(id);
    }

    @PatchMapping("/{id}")
    public Long updateCategory(@PathVariable("id") Long id,
                               @RequestBody @Valid CategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

}