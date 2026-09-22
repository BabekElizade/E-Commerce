package com.babakalizada.product.controller.impl;

import org.springframework.data.domain.Page;
import com.babakalizada.product.controller.IRestProductController;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;
import com.babakalizada.product.sevice.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestProductControllerImpl implements IRestProductController {

    private final IProductService productService;

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoProductResponse createProduct(@Valid @RequestBody DtoProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping(path = "/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoProductResponse getProductById(@PathVariable(name = "id") Long id) {
        return productService.getProductById(id);
    }

    @GetMapping(path = "/list")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Page<DtoProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return productService.getAllProducts(pageable);
    }

    @PutMapping(path = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoProductResponse updateProduct(@PathVariable(name = "id") Long id, @Valid @RequestBody DtoUpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping(path = "/change-status/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoProductResponse changeProductStatus(@PathVariable(name = "id") Long id, @RequestBody DtoChangeStatusRequest status) {
        return productService.changeProductStatus(id, status);
    }

    @GetMapping(path = "/get-by-category/{category_id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Page<DtoProductResponse> getProductsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable(name = "category_id") Long categoryId
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return productService.getProductsByCategory(categoryId, pageable);
    }

    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Page<DtoProductResponse> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "query") String query
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return productService.searchProducts(query, pageable);
    }
}
