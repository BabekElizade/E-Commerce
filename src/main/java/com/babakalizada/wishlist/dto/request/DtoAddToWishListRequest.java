package com.babakalizada.wishlist.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoAddToWishListRequest {
    @NotNull
    private Long wishListId;
    @NotNull
    private Long productId;
}
