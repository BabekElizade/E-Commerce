package com.babakalizada.product.dto.request;

import com.babakalizada.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoChangeStatusRequest {
    private ProductStatus status;
}
