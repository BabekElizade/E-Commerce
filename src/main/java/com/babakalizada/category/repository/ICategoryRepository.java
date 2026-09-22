package com.babakalizada.category.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Page<Category> findByParentCategoryIsNull(Pageable pageable);

    @Query(value = "SELECT * FROM categories c WHERE c.parent_id = :parentId", nativeQuery = true)
    List<Category> findSubCategoriesByParentId(@Param("parentId") Long parentId);
}