package com.babakalizada.order.controller;

import org.springframework.data.domain.Page;
import com.babakalizada.order.dto.response.DtoOrderResponse;


public interface IRestOrderController {
    DtoOrderResponse createOrder();

    Page<DtoOrderResponse> getMyOrders(int page, int size);

    DtoOrderResponse getMyOrder(Long orderId);

    void cancelOrder(Long orderId);
}
