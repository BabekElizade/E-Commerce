package com.babakalizada.review.dto.request;

import com.babakalizada.review.enums.RatingScore;
import com.babakalizada.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoCreateReviewRequest {
    @NotNull(message = "ID cannot be null")
    private Long id;
    @NotNull(message = "Rating is required")
    private RatingScore rating;
    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 2000)
    private String comment;
}
