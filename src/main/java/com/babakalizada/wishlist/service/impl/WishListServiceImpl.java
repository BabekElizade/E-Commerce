package com.babakalizada.wishlist.service.impl;

import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import com.babakalizada.wishlist.dto.request.DtoAddToWishListRequest;
import com.babakalizada.wishlist.dto.response.DtoWishListItemResponse;
import com.babakalizada.wishlist.entity.WishList;
import com.babakalizada.wishlist.entity.WishListItems;
import com.babakalizada.wishlist.repository.IWishItemsRepository;
import com.babakalizada.wishlist.repository.IWishListRepository;
import com.babakalizada.wishlist.service.IWishListService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements IWishListService {

    private final IWishListRepository wishListRepository;
    private final IWishItemsRepository wishListItemRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Transactional
    @Override
    public DtoWishListItemResponse addItem(
            DtoAddToWishListRequest request
    ) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found")
                );

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findUsersByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        WishList wishList = wishListRepository
                .findByUserId(user.getId())
                .orElseGet(() -> {

                    WishList newWishList = WishList.builder()
                            .user(user)
                            .build();

                    return wishListRepository.save(newWishList);
                });

        Optional<WishListItems> existingItem =
                wishListItemRepository
                        .findByWishlist_IdAndProduct_Id(
                                wishList.getId(),
                                product.getId()
                        );

        if (existingItem.isPresent()) {
            throw new IllegalArgumentException(
                    "Product already exists in wishlist"
            );
        }

        WishListItems item = WishListItems.builder()
                .wishlist(wishList)
                .product(product)
                .build();

        WishListItems savedItem =
                wishListItemRepository.save(item);

        return mapToResponse(savedItem);
    }


    @Override
    public List<DtoWishListItemResponse> getItemsById(Long id) {

        WishList wishList = wishListRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wishlist not found")
                );

        return wishList.getItems()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @Override
    public void removeItem(Long id) {

        WishListItems item = wishListItemRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wishlist item not found"
                        )
                );

        wishListItemRepository.delete(item);
    }


    @Transactional
    @Override
    public void clearWishList(Long id) {

        WishList wishList = wishListRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wishlist not found")
                );

        wishList.getItems().clear();
    }


    private DtoWishListItemResponse mapToResponse(
            WishListItems item
    ) {

        return DtoWishListItemResponse.builder()
                .itemId(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .build();
    }
}
