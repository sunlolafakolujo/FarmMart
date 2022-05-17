package com.farmmart.service.category;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.repository.category.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class CategoryServiceImpTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService=new CategoryServiceImp();

    Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category=new Category();
    }

    @Test
    void testThatYouCanMockSaveCategoryMethod() throws CategoryNotFoundException {

        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        categoryService.saveCategory(category);

        ArgumentCaptor<Category> categoryArgumentCaptor=ArgumentCaptor.forClass(Category.class);

        Mockito.verify(categoryRepository,Mockito.times(1)).save(categoryArgumentCaptor.capture());

        Category capturedCategory=categoryArgumentCaptor.getValue();

        assertEquals(capturedCategory,category);
    }

    @Test
    void testThatYouCanMockFindCategoryByIdMethod() throws CategoryNotFoundException {

        Long id =26L;
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.findCategoryById(id);

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindCategoryByNameMethod() throws CategoryNotFoundException {
        String name="Vegetable";

        Mockito.when(categoryRepository.findByCategoryName(name)).thenReturn(category);

        categoryService.findCategoryByName(name);

        Mockito.verify(categoryRepository, Mockito.times(1)).findByCategoryName(name);
    }

    @Test
    void testThatYouCanMockFindAllCategoriesMethod() {

        List<Category> categoryList=new ArrayList<>();

        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);

        categoryService.findAllCategories(10);

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockUpdateCategoryMethod() throws CategoryNotFoundException {
        Long id=25L;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.updateCategory(category, id);

        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }

    @Test
    void testThatYouCanMockDeleteCategoryByIdMethod() throws CategoryNotFoundException {
        Long id=30L;

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteCategoryById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllCategoriesMethod() {
        doNothing().when(categoryRepository).deleteAll();

        categoryService.deleteAllCategories();

        verify(categoryRepository, times(1)).deleteAll();
    }
}