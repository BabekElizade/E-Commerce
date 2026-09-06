package com.babakalizada.cart.controller;

import com.babakalizada.cart.dto.request.DtoAddToCartRequest;
import com.babakalizada.cart.dto.response.DtoCartItemResponse;
import com.babakalizada.cart.dto.response.DtoCartResponse;
import com.babakalizada.cart.dto.request.DtoUpdateItemQuantityRequest;

public interface IRestCartController {
    DtoCartItemResponse addToCart(DtoAddToCartRequest request);

    DtoCartResponse getCartById(Long id);

    void removeItem(Long id);

    DtoCartItemResponse updateItemQuantity(
            Long id,
            DtoUpdateItemQuantityRequest request
    );

    void clearCartById(Long id);
}
