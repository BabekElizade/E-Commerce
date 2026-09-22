package com.babakalizada.order.controller.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;

import com.babakalizada.order.controller.IRestOrderController;
import com.babakalizada.order.dto.response.DtoOrderResponse;
import com.babakalizada.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class RestOrderControllerImpl
        implements IRestOrderController {

    private final IOrderService orderService;


    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoOrderResponse createOrder() {
        return orderService.createOrder();
    }


    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Page<DtoOrderResponse> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return orderService.getMyOrders(pageable);
    }


    @GetMapping("/me/{orderId}")
    @Override
    public DtoOrderResponse getMyOrder(
            @PathVariable Long orderId
    ) {
        return orderService.getMyOrder(orderId);
    }


    @PatchMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void cancelOrder(
            @PathVariable Long orderId
    ) {
        orderService.cancelOrder(orderId);
    }
}