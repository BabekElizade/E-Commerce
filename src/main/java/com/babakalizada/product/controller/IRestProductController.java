package com.babakalizada.product.controller;

import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;

import java.util.List;

public interface IRestProductController {
    DtoProductResponse createProduct(DtoProductRequest request);

    DtoProductResponse getProductById(Long id);

    List<DtoProductResponse> getAllProducts();

    DtoProductResponse updateProduct(Long id, DtoUpdateProductRequest request);

    void deleteProduct(Long id);

    DtoProductResponse changeProductStatus(Long id, DtoChangeStatusRequest status);

    List<DtoProductResponse> getProductsByCategory(Long categoryId);

    List<DtoProductResponse> searchProducts(String query);
}
