package com.babakalizada.payment.controller.impl;

import com.babakalizada.payment.controller.IRestPaymentController;
import com.babakalizada.payment.dto.request.DtoPaymentRequest;
import com.babakalizada.payment.dto.response.DtoPaymentResponse;
import com.babakalizada.payment.service.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestPaymentControllerImpl implements IRestPaymentController {

    private final IPaymentService paymentService;

    @PostMapping(path = "/pay/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoPaymentResponse pay(@PathVariable(name = "id") Long id) {
        return paymentService.pay(id);
    }

    @GetMapping(path = "/by-order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoPaymentResponse getPaymentByOrderId(@PathVariable(name = "orderId") Long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
}
