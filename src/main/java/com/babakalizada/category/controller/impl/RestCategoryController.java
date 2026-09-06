package com.babakalizada.category.controller.impl;

import com.babakalizada.category.controller.IRestCategoryController;
import com.babakalizada.category.dto.response.DtoCategoryResponse;
import com.babakalizada.category.dto.request.DtoCreateCategoryRequest;
import com.babakalizada.category.dto.request.DtoUpdateCategoryRequest;
import com.babakalizada.category.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/e-commerce")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestCategoryController implements IRestCategoryController {

    private final ICategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create")
    @Override
    public DtoCategoryResponse createCategory(@Valid @RequestBody DtoCreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/category/{id}")
    @Override
    public DtoCategoryResponse getCategoryById(@PathVariable(name = "id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/parent-category")
    @Override
    public List<DtoCategoryResponse> getAllParentCategories() {
        return categoryService.getAllParentCategories();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/update-category/{id}")
    @Override
    public DtoCategoryResponse updateCategory(@PathVariable(name = "id") Long id, @Valid @RequestBody DtoUpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
    }
}
