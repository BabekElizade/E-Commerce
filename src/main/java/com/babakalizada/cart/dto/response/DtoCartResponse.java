package com.babakalizada.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoCartResponse {
    private Long id;

    private List<DtoCartItemResponse> items;

    private Integer totalQuantity;

    private BigDecimal totalPrice;
}
