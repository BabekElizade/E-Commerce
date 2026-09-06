package com.babakalizada.product.controller.impl;

import com.babakalizada.product.controller.IRestProductController;
import com.babakalizada.product.dto.request.DtoChangeStatusRequest;
import com.babakalizada.product.dto.request.DtoProductRequest;
import com.babakalizada.product.dto.response.DtoProductResponse;
import com.babakalizada.product.dto.request.DtoUpdateProductRequest;
import com.babakalizada.product.sevice.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<DtoProductResponse> getAllProducts() {
        return productService.getAllProducts();
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
    public List<DtoProductResponse> getProductsByCategory(@PathVariable(name = "category_id") Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<DtoProductResponse> searchProducts(@RequestParam(name = "query") String query) {
        return productService.searchProducts(query);
    }
}
