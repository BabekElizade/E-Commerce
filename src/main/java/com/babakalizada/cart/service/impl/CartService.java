package com.babakalizada.cart.service.impl;

import com.babakalizada.cart.dto.request.DtoAddToCartRequest;
import com.babakalizada.cart.dto.response.DtoCartItemResponse;
import com.babakalizada.cart.dto.response.DtoCartResponse;
import com.babakalizada.cart.dto.request.DtoUpdateItemQuantityRequest;
import com.babakalizada.cart.entity.Cart;
import com.babakalizada.cart.entity.CartItem;
import com.babakalizada.cart.repository.ICartItemRepository;
import com.babakalizada.cart.repository.ICartRepository;
import com.babakalizada.cart.service.ICartService;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.product.entity.Product;
import com.babakalizada.product.enums.ProductStatus;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Transactional
    @Override
    public DtoCartItemResponse addToCart(DtoAddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Product not found!"
                                )
                        )
                );

        if(product.getStatus() != ProductStatus.ACTIVE){
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.INVALID_PRODUCT,
                            "Invalid product status: " + product.getName()
                    )
            );
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findUsersByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.USER_NOT_FOUND,
                                        "User not found!"
                                )
                        )
                );

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });


        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .build();

        CartItem savedItem = cartItemRepository.save(cartItem);

        DtoCartItemResponse response = new DtoCartItemResponse();
        BeanUtils.copyProperties(savedItem, response);
        response.setProductId(product.getId());
        response.setProductName(product.getName());

        return response;
    }

    @Override
    public DtoCartResponse getCartById(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Cart not found!"
                                )
                        )
                );

        List<DtoCartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> DtoCartItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()
                )
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(DtoCartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantity = items.stream()
                .map(DtoCartItemResponse::getQuantity)
                .reduce(0, Integer::sum);

        return DtoCartResponse.builder()
                .id(cart.getId())
                .items(items)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    @Override
    public void removeItem(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Cart item not found!"
                    )
            );
        }
        cartItemRepository.deleteById(id);
    }

    @Transactional
    @Override
    public DtoCartItemResponse updateItemQuantity(Long id, DtoUpdateItemQuantityRequest request) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Cart item not found!"
                    )
            );
        }
        cartItem.get().setQuantity(request.getQuantity());
        CartItem savedItem = cartItemRepository.save(cartItem.get());
        DtoCartItemResponse response = new DtoCartItemResponse();
        BeanUtils.copyProperties(savedItem, response);
        response.setProductId(cartItem.get().getProduct().getId());
        response.setProductName(cartItem.get().getProduct().getName());
        return response;
    }

    @Transactional
    @Override
    public void clearCartById(Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Cart item not found!"
                    )
            );
        }
        cart.get().getItems().clear();
        cartRepository.save(cart.get());
    }
}
