package com.babakalizada.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name,
            String description,
            String sku,
            Pageable pageable
    );
}
