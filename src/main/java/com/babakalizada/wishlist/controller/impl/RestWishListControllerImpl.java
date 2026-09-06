package com.babakalizada.wishlist.controller.impl;

import com.babakalizada.wishlist.controller.IRestWishListController;
import com.babakalizada.wishlist.dto.request.DtoAddToWishListRequest;
import com.babakalizada.wishlist.dto.response.DtoWishListItemResponse;
import com.babakalizada.wishlist.service.IWishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestWishListControllerImpl implements IRestWishListController {

    private final IWishListService wishListService;

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoWishListItemResponse addItem(@Valid @RequestBody DtoAddToWishListRequest dtoAddToWishListRequest) {
        return wishListService.addItem(dtoAddToWishListRequest);
    }

    @GetMapping(path = "/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<DtoWishListItemResponse> getItemsById(@PathVariable(name = "id") Long id) {
        return wishListService.getItemsById(id);
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void removeItem(@PathVariable(name = "id") Long id) {
        wishListService.removeItem(id);
    }

    @DeleteMapping(path = "/clear/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void clearWishList(@PathVariable(name = "id") Long id) {
        wishListService.clearWishList(id);
    }
}
