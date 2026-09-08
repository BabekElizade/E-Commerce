package com.babakalizada.product.sevice.impl;

import com.babakalizada.category.repository.ICategoryRepository;
import com.babakalizada.product.enums.ProductStatus;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;
import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.product.search_engine.SearchEngine;
import com.babakalizada.product.sevice.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final SearchEngine searchEngine;

    @Transactional
    @Override
    public DtoProductResponse createProduct(DtoProductRequest request) {
        if (request.getPrice() == null
                || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (request.getStock() == null || request.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .sku(request.getSku())
                .categoryId(categoryRepository.findById(request.getCategoryId()).get().getId())
                .status(ProductStatus.ACTIVE)
                .createdAt(new Date())
                .build();

        Product savedProduct = productRepository.save(product);

        DtoProductResponse response = new DtoProductResponse();
        BeanUtils.copyProperties(savedProduct, response);

        return response;
    }

    @Override
    public DtoProductResponse getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new IllegalArgumentException("Product not found");
        }
        DtoProductResponse response = new DtoProductResponse();
        BeanUtils.copyProperties(product.get(), response);
        return response;
    }

    @Override
    public List<DtoProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<DtoProductResponse> dtoResponse = new ArrayList<>();
        for (Product product : products) {
            if(product == null){
                throw new IllegalArgumentException("Product not found");
            }
            DtoProductResponse response = new DtoProductResponse();
            BeanUtils.copyProperties(product, response);
            dtoResponse.add(response);
        }
        return dtoResponse;
    }

    @Transactional
    @Override
    public DtoProductResponse updateProduct(Long id, DtoUpdateProductRequest request) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) throw new IllegalArgumentException("Product not found");
        if(request.getName() != null) product.get().setName(request.getName());
        if(request.getDescription() != null) product.get().setDescription(request.getDescription());
        if(request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) > 0) product.get().setPrice(request.getPrice());
        if(request.getStock() != null && request.getStock() > 0) product.get().setStock(request.getStock());
        if(request.getCategoryId() != null) product.get().setCategoryId(request.getCategoryId());
        product.get().setUpdatedAt(new Date());

        Product savedProducts = productRepository.save(product.get());
        DtoProductResponse response = new DtoProductResponse();
        BeanUtils.copyProperties(savedProducts, response);
        return response;
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    @Override
    public DtoProductResponse changeProductStatus(Long id, DtoChangeStatusRequest status) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new IllegalArgumentException("Product not found");
        }
        product.get().setStatus(status.getStatus());
        productRepository.save(product.get());
        DtoProductResponse response = new DtoProductResponse();
        BeanUtils.copyProperties(product.get(), response);
        return response;
    }

    @Override
    public List<DtoProductResponse> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found");
        }

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(product -> {
                    DtoProductResponse response = new DtoProductResponse();
                    BeanUtils.copyProperties(product, response);
                    response.setCategoryId(product.getCategoryId());
                    return response;
                })
                .toList();
    }

    @Override
    public List<DtoProductResponse> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return searchEngine.search(query)
                .stream()
                .map(product -> {
                    DtoProductResponse response = new DtoProductResponse();
                    BeanUtils.copyProperties(product, response);
                    response.setCategoryId(product.getCategoryId());
                    return response;
                })
                .toList();
    }
}
