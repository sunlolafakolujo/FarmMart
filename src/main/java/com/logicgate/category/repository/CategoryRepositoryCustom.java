package com.logicgate.category.repository;


import com.logicgate.category.model.Category;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepositoryCustom {
    @Query("FROM Category c WHERE c.categoryCode=?1 OR c.categoryName=?2")
    Optional<Category> findCategoryByCodeOrName(String categoryCode, String categoryName);
}
