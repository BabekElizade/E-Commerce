package com.babakalizada.product.controller;

import org.springframework.data.domain.Page;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;


public interface IRestProductController {
    DtoProductResponse createProduct(DtoProductRequest request);

    DtoProductResponse getProductById(Long id);

    Page<DtoProductResponse> getAllProducts(int page, int size);

    DtoProductResponse updateProduct(Long id, DtoUpdateProductRequest request);

    void deleteProduct(Long id);

    DtoProductResponse changeProductStatus(Long id, DtoChangeStatusRequest status);

    Page<DtoProductResponse> getProductsByCategory(int page, int size, Long categoryId);

    Page<DtoProductResponse> searchProducts(int page, int size, String query);
}
