package com.babakalizada.order.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.order.dto.response.DtoOrderResponse;


public interface IOrderService {

    DtoOrderResponse createOrder();

    Page<DtoOrderResponse> getMyOrders(Pageable pageable);

    DtoOrderResponse getMyOrder(Long orderId);

    void cancelOrder(Long orderId);
}