package com.babakalizada.category.controller;

import org.springframework.data.domain.Page;
import com.babakalizada.category.dto.response.DtoCategoryResponse;
import com.babakalizada.category.dto.request.DtoCreateCategoryRequest;
import com.babakalizada.category.dto.request.DtoUpdateCategoryRequest;


public interface IRestCategoryController {
    DtoCategoryResponse createCategory(DtoCreateCategoryRequest request);

    DtoCategoryResponse getCategoryById(Long id);

    Page<DtoCategoryResponse> getAllParentCategories(int page, int size);

    DtoCategoryResponse updateCategory(Long id, DtoUpdateCategoryRequest request);

    void deleteCategory(Long id);
}
