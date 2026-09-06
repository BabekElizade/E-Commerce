package com.babakalizada.wishlist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoWishListItemResponse {

    private Long itemId;

    private Long productId;

    private String productName;

    private BigDecimal price;

}
