package com.babakalizada.cart.controller.impl;

import com.babakalizada.cart.controller.IRestCartController;
import com.babakalizada.cart.dto.request.DtoAddToCartRequest;
import com.babakalizada.cart.dto.response.DtoCartItemResponse;
import com.babakalizada.cart.dto.response.DtoCartResponse;
import com.babakalizada.cart.dto.request.DtoUpdateItemQuantityRequest;
import com.babakalizada.cart.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestCartControllerImpl implements IRestCartController {

    private final ICartService cartService;

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoCartItemResponse addToCart(@Valid @RequestBody DtoAddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoCartResponse getCartById(@PathVariable(name = "id") Long id) {
        return cartService.getCartById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void removeItem(@PathVariable(name = "id") Long id) {
        cartService.removeItem(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoCartItemResponse updateItemQuantity(@PathVariable(name = "id") Long id, @Valid @RequestBody DtoUpdateItemQuantityRequest request) {
        return cartService.updateItemQuantity(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/clear/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void clearCartById(@PathVariable(name = "id") Long id) {
        cartService.clearCartById(id);
    }
}
