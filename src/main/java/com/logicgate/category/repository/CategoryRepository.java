package com.logicgate.category.repository;


import com.logicgate.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>, CategoryRepositoryCustom {
}
