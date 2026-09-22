package com.babakalizada.wishlist.controller;

import org.springframework.data.domain.Page;
import com.babakalizada.wishlist.dto.request.DtoAddToWishListRequest;
import com.babakalizada.wishlist.dto.response.DtoWishListItemResponse;


public interface IRestWishListController {
    DtoWishListItemResponse addItem(DtoAddToWishListRequest dtoAddToWishListRequest);

    Page<DtoWishListItemResponse> getItemsById(Long id, int page, int size);

    void removeItem(Long id);

    void clearWishList(Long id);
}
