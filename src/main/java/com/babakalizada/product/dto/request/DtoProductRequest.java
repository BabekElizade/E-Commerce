package com.babakalizada.product.dto.request;

import com.babakalizada.product.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoProductRequest {

    @NotBlank(message = "Name can't be empty!")
    private String name;

    private String description;

    @NotNull(message = "Price can't be empty!")
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @NotBlank
    private String sku;

    @NotNull(message = "Category is required!")
    private Long categoryId;

    private ProductStatus status = ProductStatus.ACTIVE;

    private Date createdAt;

    private Date updatedAt;
}
