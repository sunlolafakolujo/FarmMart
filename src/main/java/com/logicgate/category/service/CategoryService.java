package com.logicgate.category.service;



import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category) throws CategoryNotFoundException;
    Category fetchCategoryById(Long id) throws CategoryNotFoundException;
    Category fetchCategoryByCodeOrName(String searchKey) throws CategoryNotFoundException;
    List<Category> fetchAllCategory(Integer pageNumber);
    Category updateCategory(Category category,Long id) throws CategoryNotFoundException;
    void deleteCategory(Long id) throws CategoryNotFoundException;
    void deleteAllCategory();
}
