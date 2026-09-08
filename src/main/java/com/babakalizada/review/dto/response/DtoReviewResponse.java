package com.babakalizada.review.dto.response;

import com.babakalizada.product.entity.Product;
import com.babakalizada.review.enums.RatingScore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoReviewResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private RatingScore rating;
    private String comment;
}
