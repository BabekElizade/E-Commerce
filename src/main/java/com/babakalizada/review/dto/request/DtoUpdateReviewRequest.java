package com.babakalizada.review.dto.request;

import com.babakalizada.review.enums.RatingScore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoUpdateReviewRequest {
    private String comment;
    private RatingScore rating;
}
