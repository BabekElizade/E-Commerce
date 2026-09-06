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
import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
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

    @Override
    public DtoCartItemResponse addToCart(DtoAddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found")
                );

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findUsersByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
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
                        new IllegalArgumentException("Cart not found")
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

    @Override
    public void removeItem(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            throw new IllegalArgumentException("Cart item not found");
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public DtoCartItemResponse updateItemQuantity(Long id, DtoUpdateItemQuantityRequest request) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isEmpty()) {
            throw new IllegalArgumentException("Cart item not found");
        }
        cartItem.get().setQuantity(request.getQuantity());
        CartItem savedItem = cartItemRepository.save(cartItem.get());
        DtoCartItemResponse response = new DtoCartItemResponse();
        BeanUtils.copyProperties(savedItem, response);
        response.setProductId(cartItem.get().getProduct().getId());
        response.setProductName(cartItem.get().getProduct().getName());
        return response;
    }

    @Override
    public void clearCartById(Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }
        cart.get().getItems().clear();
        cartRepository.save(cart.get());
    }
}
