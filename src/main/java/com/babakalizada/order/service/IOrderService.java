package com.babakalizada.order.service;

import com.babakalizada.order.dto.response.DtoOrderResponse;

import java.util.List;

public interface IOrderService {

    DtoOrderResponse createOrder();

    List<DtoOrderResponse> getMyOrders();

    DtoOrderResponse getMyOrder(Long orderId);

    void cancelOrder(Long orderId);
}