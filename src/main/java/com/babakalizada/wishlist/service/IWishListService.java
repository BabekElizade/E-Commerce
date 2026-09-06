package com.babakalizada.wishlist.service;

import com.babakalizada.wishlist.dto.request.DtoAddToWishListRequest;
import com.babakalizada.wishlist.dto.response.DtoWishListItemResponse;

import java.util.List;

public interface IWishListService {
    DtoWishListItemResponse addItem(DtoAddToWishListRequest dtoAddToWishListRequest);
    List<DtoWishListItemResponse> getItemsById(Long id);
    void removeItem(Long id);
    void clearWishList(Long id);
}
