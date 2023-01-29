package com.logicgate.category.service;


import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.category.model.Category;
import com.logicgate.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) throws CategoryNotFoundException {
        category.setCategoryCode("CAT".concat(String.valueOf(new Random().nextInt(100000))));
        Optional<Category> savedCategory=categoryRepository.findCategoryByCodeOrName(category.getCategoryCode(),
                category.getCategoryName());
        if (savedCategory.isPresent()) {
            throw new CategoryNotFoundException("Category already exist");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category fetchCategoryById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Category ID "+id+" Not Found"));
    }

    @Override
    public Category fetchCategoryByCodeOrName(String searchKey) throws CategoryNotFoundException {
        return categoryRepository.findCategoryByCodeOrName(searchKey,searchKey)
                .orElseThrow(()->new CategoryNotFoundException("Category "+searchKey+" Not Found"));
    }

    @Override
    public List<Category> fetchAllCategory(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return categoryRepository.findAll(pageable).toList();
    }

    @Override
    public Category updateCategory(Category category, Long id) throws CategoryNotFoundException {
        Category savedCategory=categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Category ID "+id+" Not Found"));
        if (Objects.nonNull(category.getCategoryName()) && !"".equalsIgnoreCase(category.getCategoryName())){
            savedCategory.setCategoryName(category.getCategoryName());
        }
        return categoryRepository.save(savedCategory);
    }

    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException {
        if (categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }else {
            throw new CategoryNotFoundException("Category ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllCategory() {
        categoryRepository.deleteAll();
    }
}
