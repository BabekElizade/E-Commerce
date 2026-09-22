package com.babakalizada.product.sevice.impl;

import org.springframework.data.domain.Page;
import com.babakalizada.category.repository.ICategoryRepository;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.NullRequestException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.product.enums.ProductStatus;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.product.search_engine.SearchEngine;
import com.babakalizada.product.sevice.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final SearchEngine searchEngine;

    @Transactional
    @Override
    public DtoProductResponse createProduct(DtoProductRequest request) {
        requireRequest(request);

        if (request.getPrice() == null
                || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Price is required and must be greater than zero"
                    )
            );
        }

        if (request.getStock() == null || request.getStock() < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Stock is required and cannot be negative"
                    )
            );
        }

        validateCategory(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .sku(request.getSku())
                .categoryId(request.getCategoryId())
                .status(ProductStatus.ACTIVE)
                .createdAt(new Date())
                .build();

        return toResponse(productRepository.save(product));
    }

    @Override
    public DtoProductResponse getProductById(Long id) {
        return toResponse(findProductOrThrow(id));
    }

    @Override
    public Page<DtoProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional
    @Override
    public DtoProductResponse updateProduct(
            Long id,
            DtoUpdateProductRequest request
    ) {
        requireRequest(request);

        Product product = findProductOrThrow(id);

        // null olan field-lər dəyişdirilmir.
        if (request.getPrice() != null
                && request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Price must be greater than zero"
                    )
            );
        }

        if (request.getStock() != null && request.getStock() < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Stock cannot be negative"
                    )
            );
        }

        if (request.getCategoryId() != null) {
            validateCategory(request.getCategoryId());
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }

        product.setUpdatedAt(new Date());

        return toResponse(productRepository.save(product));
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    @Transactional
    @Override
    public DtoProductResponse changeProductStatus(
            Long id,
            DtoChangeStatusRequest request
    ) {
        requireRequest(request);

        if (request.getStatus() == null) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Product status is required"
                    )
            );
        }

        Product product = findProductOrThrow(id);
        product.setStatus(request.getStatus());
        product.setUpdatedAt(new Date());

        return toResponse(productRepository.save(product));
    }

    @Override
    public Page<DtoProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        validateCategory(categoryId);

        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<DtoProductResponse> searchProducts(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Search query cannot be empty"
                    )
            );
        }

        return searchEngine.search(query, pageable)
                .map(this::toResponse);
    }

    private Product findProductOrThrow(Long id) {
        requireId(id, "Product ID");

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Product not found with ID: " + id
                        )
                ));
    }

    private void validateCategory(Long categoryId) {
        requireId(categoryId, "Category ID");

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Category not found with ID: " + categoryId
                    )
            );
        }
    }

    private void requireRequest(Object request) {
        if (request == null) {
            throw new NullRequestException(
                    new ErrorMessage(
                            ErrorCode.NULL_REQUEST,
                            "Request body is required"
                    )
            );
        }
    }

    private void requireId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            fieldName + " is required and must be greater than zero"
                    )
            );
        }
    }

    private DtoProductResponse toResponse(Product product) {
        DtoProductResponse response = new DtoProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }
}