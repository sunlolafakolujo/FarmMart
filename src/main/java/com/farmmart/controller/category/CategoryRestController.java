package com.farmmart.controller.categoryrestcontroller;

import com.farmmart.data.model.category.*;
import com.farmmart.service.category.CategoryServiceImp;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryServiceImp categoryServiceImp;

    private final ModelMapper modelMapper;

    @PostMapping("/saveCategory")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewCategory> saveCategory(@Valid @RequestBody NewCategory newCategory) throws CategoryNotFoundException {

        Category category=modelMapper.map(newCategory, Category.class);

        Category postCategory=categoryServiceImp.saveCategory(category);

        NewCategory postedCategory=modelMapper.map(postCategory, NewCategory.class);

        return new ResponseEntity<>(postedCategory,HttpStatus.CREATED);
    }

    @GetMapping("/findCategoryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(value = "id") Long id) throws CategoryNotFoundException {
        Category category=categoryServiceImp.findCategoryById(id);

        CategoryDto categoryDto=convertCategoryToDto(category);

        return ResponseEntity.ok().body(categoryDto);
    }

    @GetMapping("/findCategoryByName/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_ASSISTANT_ADMIN')")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable(value = "name") String name) throws CategoryNotFoundException {

        Category category=categoryServiceImp.findCategoryByName(name);

        CategoryDto categoryDto=convertCategoryToDto(category);

        return ResponseEntity.ok().body(categoryDto);
    }


    @GetMapping("/findAllCategories")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_ASSISTANT_ADMIN')")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){

        return ResponseEntity.ok().body(categoryServiceImp.findAllCategories()
                .stream()
                .map(this::convertCategoryToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateCategoryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyCategory> updateCategory(@Valid @RequestBody ModifyCategory modifyCategory,
                                                         @PathVariable(value = "id") Long id) throws CategoryNotFoundException {

        Category category=modelMapper.map(modifyCategory, Category.class);

        Category updateCategory=categoryServiceImp.updateCategory(category, id);

        ModifyCategory updatedCategory=modelMapper.map(updateCategory, ModifyCategory.class);

        return ResponseEntity.ok().body(updatedCategory);
    }

    @DeleteMapping("/deleteCategoryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCategoryById(@PathVariable(value = "id") Long id) throws CategoryNotFoundException {

        categoryServiceImp.deleteCategoryById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllCategories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllCategories(){
        categoryServiceImp.deleteAllCategories();

        return ResponseEntity.noContent().build();
    }

    private CategoryDto convertCategoryToDto(Category category){
        CategoryDto categoryDto=new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setCategoryName(category.getCategoryName());

        return categoryDto;
    }
}
