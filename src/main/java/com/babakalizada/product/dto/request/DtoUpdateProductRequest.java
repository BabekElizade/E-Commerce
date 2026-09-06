package com.babakalizada.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DtoUpdateProductRequest {
    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Long categoryId;

    private Date updatedAt;
}
