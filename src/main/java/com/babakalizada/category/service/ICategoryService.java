package com.babakalizada.category.service;

import com.babakalizada.category.dto.response.DtoCategoryResponse;
import com.babakalizada.category.dto.request.DtoCreateCategoryRequest;
import com.babakalizada.category.dto.request.DtoUpdateCategoryRequest;

import java.util.List;

public interface ICategoryService {
    DtoCategoryResponse createCategory(DtoCreateCategoryRequest request);

    DtoCategoryResponse getCategoryById(Long id);

    List<DtoCategoryResponse> getAllParentCategories();

    DtoCategoryResponse updateCategory(Long id, DtoUpdateCategoryRequest request);

    void deleteCategory(Long id);
}
