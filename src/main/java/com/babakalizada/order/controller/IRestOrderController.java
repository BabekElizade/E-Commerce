package com.babakalizada.order.controller;

import com.babakalizada.order.dto.response.DtoOrderResponse;

import java.util.List;

public interface IRestOrderController {
    DtoOrderResponse createOrder();

    List<DtoOrderResponse> getMyOrders();

    DtoOrderResponse getMyOrder(Long orderId);

    void cancelOrder(Long orderId);
}
