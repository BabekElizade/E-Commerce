package com.babakalizada.wishlist.service.impl;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.*;
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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        if (request == null) {
            throw new NullRequestException(
                    new ErrorMessage(
                            ErrorCode.NULL_REQUEST,
                            "Request body is required"
                    )
            );
        }

        requireId(request.getProductId(), "Product ID");

        User user = getCurrentUser();

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Product not found"
                        )
                ));

        WishList wishList = wishListRepository
                .findByUserId(user.getId())
                .orElseGet(() -> {
                    WishList newWishList = WishList.builder()
                            .user(user)
                            .build();

                    return wishListRepository.save(newWishList);
                });

        boolean alreadyExists = wishListItemRepository
                .findByWishlist_IdAndProduct_Id(
                        wishList.getId(),
                        product.getId()
                )
                .isPresent();

        if (alreadyExists) {
            throw new ResourceAlreadyExistsException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_ALREADY_EXISTS,
                            "Product already exists in wishlist"
                    )
            );
        }

        WishListItems item = WishListItems.builder()
                .wishlist(wishList)
                .product(product)
                .build();

        return mapToResponse(wishListItemRepository.save(item));
    }

    @Transactional
    @Override
    public List<DtoWishListItemResponse> getItemsById(Long id) {
        User user = getCurrentUser();
        WishList wishList = findWishListOrThrow(id);

        checkOwnership(wishList, user);

        return wishList.getItems()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @Override
    public void removeItem(Long id) {
        requireId(id, "Wishlist item ID");

        User user = getCurrentUser();

        WishListItems item = wishListItemRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Wishlist item not found"
                        )
                ));

        checkOwnership(item.getWishlist(), user);

        wishListItemRepository.delete(item);
    }

    @Transactional
    @Override
    public void clearWishList(Long id) {
        User user = getCurrentUser();
        WishList wishList = findWishListOrThrow(id);

        checkOwnership(wishList, user);

        // orphanRemoval konfiqurasiyasından asılı olmadan silir.
        wishListItemRepository.deleteAll(
                List.copyOf(wishList.getItems())
        );

        wishList.getItems().clear();
    }

    private WishList findWishListOrThrow(Long id) {
        requireId(id, "Wishlist ID");

        return wishListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Wishlist not found"
                        )
                ));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Authentication is required"
            );
        }

        return userRepository
                .findUsersByUsername(authentication.getName())
                .orElseThrow(() ->
                        new AuthenticationCredentialsNotFoundException(
                                "Authenticated user no longer exists"
                        )
                );
    }

    private void checkOwnership(WishList wishList, User user) {
        if (!Objects.equals(
                wishList.getUser().getId(),
                user.getId()
        )) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You cannot access this wishlist"
                    )
            );
        }
    }

    private void requireId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            fieldName
                                    + " is required and must be greater than zero"
                    )
            );
        }
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