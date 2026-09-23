package com.babakalizada.payment.service;

import com.babakalizada.payment.dto.request.DtoPaymentRequest;
import com.babakalizada.payment.dto.response.DtoPaymentResponse;

public interface IPaymentService {

    DtoPaymentResponse pay(Long id);

    DtoPaymentResponse getPaymentByOrderId(Long id);
}
