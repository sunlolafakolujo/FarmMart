package com.farmmart.service.category;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.repository.category.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@Slf4j
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) throws CategoryNotFoundException {

        return categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(Long id) throws CategoryNotFoundException {
        Category category=categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));

        return category;
    }

    @Override
    public Category findCategoryByName(String categoryName) throws CategoryNotFoundException {

        Category category=categoryRepository.findByCategoryName(categoryName);

        if (Objects.isNull(category)){
            throw new CategoryNotFoundException("Category does Not exist");
        }
        return category;
    }

    @Override
    public List<Category> findAllCategories(Integer limit) {
        return categoryRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Category updateCategory(Category category, long id) throws CategoryNotFoundException {
        Category savedCategory=categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException("Category Not Found"));

        if (Objects.nonNull(category.getCategoryName()) || !"".equalsIgnoreCase(category.getCategoryName())){
            savedCategory.setCategoryName(category.getCategoryName());
        }
        return categoryRepository.save(savedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) throws CategoryNotFoundException {

        categoryRepository.deleteById(id);

        Optional<Category> optionalCategory=categoryRepository.findById(id);

        if (optionalCategory.isPresent()){
            throw new CategoryNotFoundException("Category is Not Deleted");
        }

    }

    @Override
    public void deleteAllCategories() {

        categoryRepository.deleteAll();
    }
}
