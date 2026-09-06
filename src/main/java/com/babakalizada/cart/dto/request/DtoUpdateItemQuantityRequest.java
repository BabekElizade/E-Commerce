package com.babakalizada.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoUpdateItemQuantityRequest {
    @NotNull(message = "Quantity can't be empty!")
    @Positive(message = "Quantity must be greater than zero!")
    private Integer quantity;

}
