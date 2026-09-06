package com.babakalizada.order.dto.response;

import com.babakalizada.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoOrderResponse {
    private Long orderId;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private List<DtoOrderItemResponse> items;

    private LocalDateTime createdAt;
}
