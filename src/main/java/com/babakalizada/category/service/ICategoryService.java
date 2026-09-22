package com.babakalizada.category.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.category.dto.response.DtoCategoryResponse;
import com.babakalizada.category.dto.request.DtoCreateCategoryRequest;
import com.babakalizada.category.dto.request.DtoUpdateCategoryRequest;


public interface ICategoryService {
    DtoCategoryResponse createCategory(DtoCreateCategoryRequest request);

    DtoCategoryResponse getCategoryById(Long id);

    Page<DtoCategoryResponse> getAllParentCategories(Pageable pageable);

    DtoCategoryResponse updateCategory(Long id, DtoUpdateCategoryRequest request);

    void deleteCategory(Long id);
}
