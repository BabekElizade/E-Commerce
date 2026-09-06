package com.babakalizada.category.repository;

import com.babakalizada.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM categories c WHERE c.parent_id IS NULL", nativeQuery = true)
    List<Category> findByParentCategoryIsNull();

    @Query(value = "SELECT * FROM categories c WHERE c.parent_id = :parentId", nativeQuery = true)
    List<Category> findSubCategoriesByParentId(@Param("parentId") Long parentId);
}