package com.babakalizada.product.sevice;

import org.springframework.data.domain.Page;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;
import org.springframework.data.domain.Pageable;


public interface IProductService {
    DtoProductResponse createProduct(DtoProductRequest request);

    DtoProductResponse getProductById(Long id);

    Page<DtoProductResponse> getAllProducts(Pageable pageable);

    DtoProductResponse updateProduct(Long id, DtoUpdateProductRequest request);

    void deleteProduct(Long id);

    DtoProductResponse changeProductStatus(Long id, DtoChangeStatusRequest status);

    Page<DtoProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    Page<DtoProductResponse> searchProducts(String query, Pageable pageable);
}
