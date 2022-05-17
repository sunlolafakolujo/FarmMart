package com.farmmart.service.category;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    Category saveCategory(Category category) throws CategoryNotFoundException;
    Category findCategoryById(Long id) throws CategoryNotFoundException;
    Category findCategoryByName(String categoryName) throws CategoryNotFoundException;
    List<Category> findAllCategories(Integer limit);
    Category updateCategory(Category category,long id) throws CategoryNotFoundException;
    void deleteCategoryById(Long id) throws CategoryNotFoundException;
    void deleteAllCategories();
}
