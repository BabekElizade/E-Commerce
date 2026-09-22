package com.babakalizada.order.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.cart.entity.Cart;
import com.babakalizada.cart.repository.ICartRepository;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.InvalidOrderStatusException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.order.dto.response.DtoOrderItemResponse;
import com.babakalizada.order.dto.response.DtoOrderResponse;
import com.babakalizada.order.entity.Order;
import com.babakalizada.order.entity.OrderItem;
import com.babakalizada.order.enums.OrderStatus;
import com.babakalizada.order.repository.IOrderRepository;
import com.babakalizada.order.service.IOrderService;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;


    @Transactional
    @Override
    public DtoOrderResponse createOrder() {

        User user = getCurrentUser();

        Cart cart = cartRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Cart item not found!"
                                )
                        )
                );

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Cart items not found!"
                    )
            );
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();


        List<OrderItem> orderItems = cart.getItems()
                .stream()
                .map(cartItem -> {

                    if (cartItem.getProduct().getStock() < cartItem.getQuantity()) {
                        throw new BusinessException(
                                new ErrorMessage(
                                        ErrorCode.INSUFFICIENT_STOCK,
                                        "Not enough stock for product: "
                                                + cartItem.getProduct().getName()
                                )
                        );
                    }

                    BigDecimal price = cartItem.getProduct().getPrice();

                    BigDecimal totalPrice = price.multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

                    return OrderItem.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .productName(cartItem.getProduct().getName())
                            .price(price)
                            .quantity(cartItem.getQuantity())
                            .totalPrice(totalPrice)
                            .build();
                })
                .toList();


        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }


    @Override
    public Page<DtoOrderResponse> getMyOrders(Pageable pageable) {

        User user = getCurrentUser();

        return orderRepository
                .findByUser_IdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(this::mapToResponse);
    }


    @Override
    public DtoOrderResponse getMyOrder(Long orderId) {

        User user = getCurrentUser();

        Order order = orderRepository
                .findByIdAndUser_Id(orderId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found!"
                                )
                        )
                );

        return mapToResponse(order);
    }


    @Transactional
    @Override
    public void cancelOrder(Long orderId) {

        User user = getCurrentUser();

        Order order = orderRepository
                .findByIdAndUser_Id(orderId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found!"
                                )
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(
                    new ErrorMessage(
                            ErrorCode.INVALID_ORDER_STATUS,
                            "Only pending orders can be cancelled"
                    )
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
    }


    private User getCurrentUser() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findUsersByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.USER_NOT_FOUND,
                                        "User not found!"
                                )
                        )
                );
    }


    private DtoOrderResponse mapToResponse(Order order) {

        List<DtoOrderItemResponse> items = order.getItems()
                .stream()
                .map(item ->
                        DtoOrderItemResponse.builder()
                                .itemId(item.getId())
                                .productId(item.getProduct().getId())
                                .productName(item.getProductName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .totalPrice(item.getTotalPrice())
                                .build()
                )
                .toList();


        return DtoOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .createdAt(order.getCreatedAt())
                .build();
    }
}