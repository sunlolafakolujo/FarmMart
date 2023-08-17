package com.logicgate.category.controller;


import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.category.model.Category;
import com.logicgate.category.model.CategoryDto;
import com.logicgate.category.model.NewCategory;
import com.logicgate.category.model.UpdateCategory;
import com.logicgate.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<NewCategory> addCategory(@RequestBody NewCategory newCategory) throws CategoryNotFoundException {
        Category category=modelMapper.map(newCategory,Category.class);
        Category post=categoryService.addCategory(category);
        NewCategory posted=modelMapper.map(post,NewCategory.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findCategoryById")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<CategoryDto> getCategoryById(@RequestParam("id") long id) throws CategoryNotFoundException {
        Category category= categoryService.fetchCategoryById(id);
        return new ResponseEntity<>(convertCategoryToDto(category),OK);
    }

    @GetMapping("/findCategoryByCodeOrName")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<CategoryDto> getCategoryByCodeOrName(@RequestParam("searchKey") String searchKey)
                                                                                throws CategoryNotFoundException {
        Category category= categoryService.fetchCategoryByCodeOrName(searchKey);
        return new ResponseEntity<>(convertCategoryToDto(category),OK);
    }

    @GetMapping("/findAllCategories")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(categoryService.fetchAllCategory(pageNumber)
                .stream()
                .map(this::convertCategoryToDto)
                .collect(Collectors.toList()),OK);
    }

    @PutMapping("/updateCategory")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<UpdateCategory> editCategory(@RequestBody UpdateCategory updateCategory,
                                                       @RequestParam("id") Long id) throws CategoryNotFoundException {
        Category category=modelMapper.map(updateCategory,Category.class);
        Category post=categoryService.updateCategory(category,id);
        UpdateCategory posted=modelMapper.map(post,UpdateCategory.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteCategory")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<?> deleteCategoryById(@RequestParam("id") Long id) throws CategoryNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("Category "+id+" Is Deleted");
    }

    @DeleteMapping("/deleteAllCategories")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<?> deleteAllCategory(){
        categoryService.deleteAllCategory();
        return ResponseEntity.ok().body("Categories has been Deleted");
    }

    private CategoryDto convertCategoryToDto(Category category) {
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setCategoryCode(category.getCategoryCode());
        categoryDto.setCategoryName(category.getCategoryName());
        return categoryDto;
    }
}
