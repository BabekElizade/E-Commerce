package com.babakalizada.payment.controller;

import com.babakalizada.payment.dto.request.DtoPaymentRequest;
import com.babakalizada.payment.dto.response.DtoPaymentResponse;

public interface IRestPaymentController {

    DtoPaymentResponse pay(Long id);

    DtoPaymentResponse getPaymentByOrderId(Long id);
}
