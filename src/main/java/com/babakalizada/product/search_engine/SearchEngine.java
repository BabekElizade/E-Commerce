package com.babakalizada.product.search_engine;

import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchEngine {
    private final IProductRepository productRepository;

    public List<Product> search(String query) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        return productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrSkuContainingIgnoreCase(
                        query,
                        query,
                        query
                );
    }
}
