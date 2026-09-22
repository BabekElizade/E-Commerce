package com.babakalizada.wishlist.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.wishlist.dto.request.DtoAddToWishListRequest;
import com.babakalizada.wishlist.dto.response.DtoWishListItemResponse;


public interface IWishListService {
    DtoWishListItemResponse addItem(DtoAddToWishListRequest dtoAddToWishListRequest);

    Page<DtoWishListItemResponse> getItemsById(Long id, Pageable pageable);

    void removeItem(Long id);

    void clearWishList(Long id);
}
