package com.babakalizada.category.service.impl;

import com.babakalizada.category.dto.response.DtoCategoryResponse;
import com.babakalizada.category.dto.request.DtoCreateCategoryRequest;
import com.babakalizada.category.dto.request.DtoUpdateCategoryRequest;
import com.babakalizada.category.entity.Category;
import com.babakalizada.category.repository.ICategoryRepository;
import com.babakalizada.category.service.ICategoryService;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Override
    public DtoCategoryResponse createCategory(DtoCreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Bu adda kateqoriya artıq mövcuddur: " + request.getName()
                    )
            );
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        if (request.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            new ErrorMessage(
                                    ErrorCode.RESOURCE_NOT_FOUND,
                                    "Valideyn kateqoriya tapılmadı ID: " + request.getParentCategoryId()
                            )
                    ));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);

        DtoCategoryResponse response = DtoCategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();

        if (savedCategory.getParentCategory() != null) {
            response.setParentCategoryId(savedCategory.getParentCategory().getId());
            response.setParentCategoryName(savedCategory.getParentCategory().getName());
        }

        return response;
    }

    @Override
    public DtoCategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Kateqoriya tapılmadı ID: " + id
                        )
                ));

        return mapToResponse(category);
    }

    private DtoCategoryResponse mapToResponse(Category category) {
        DtoCategoryResponse response = DtoCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();

        if (category.getParentCategory() != null) {
            response.setParentCategoryId(category.getParentCategory().getId());
            response.setParentCategoryName(category.getParentCategory().getName());
        }

        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            List<DtoCategoryResponse> subDTOs = category.getSubCategories().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            response.setSubCategories(subDTOs);
        } else {
            response.setSubCategories(new ArrayList<>());
        }

        return response;
    }

    @Override
    public List<DtoCategoryResponse> getAllParentCategories() {
        List<Category> parents = categoryRepository.findByParentCategoryIsNull();
        List<DtoCategoryResponse> parentDTOs = new ArrayList<>();

        for (Category parent : parents) {
            parentDTOs.add(mapToResponse(parent));
        }

        return parentDTOs;
    }

    @Override
    public DtoCategoryResponse updateCategory(Long id, DtoUpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Kateqoriya tapılmadı ID: " + id
                        )
                ));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new BusinessException(
                        new ErrorMessage(
                                ErrorCode.BUSINESS_ERROR,
                                "Bu adda kateqoriya artıq mövcuddur: " + request.getName()
                        )
                );
            }
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        if (request.getParentCategoryId() != null) {
            if (request.getParentCategoryId().equals(id)) {
                throw new BusinessException(
                        new ErrorMessage(
                                ErrorCode.BUSINESS_ERROR,
                                "Bir kateqoriya öz-özünün valideyni ola bilməz. ID: " + id
                        )
                );
            }

            Category newParent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            new ErrorMessage(
                                    ErrorCode.RESOURCE_NOT_FOUND,
                                    "Valideyn kateqoriya tapılmadı ID: " + id
                            )
                    ));

            category.setParentCategory(newParent);
        } else {
            category.setParentCategory(null);
        }

        Category updatedCategory = categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Silinəcək kateqoriya tapılmadı ID: " + id
                        )
                ));
        categoryRepository.delete(category);
    }
}
