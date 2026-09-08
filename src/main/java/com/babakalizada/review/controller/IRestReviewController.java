package com.babakalizada.review.controller;

import com.babakalizada.review.dto.request.DtoCreateReviewRequest;
import com.babakalizada.review.dto.request.DtoUpdateReviewRequest;
import com.babakalizada.review.dto.response.DtoReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestReviewController {
    DtoReviewResponse createReview(DtoCreateReviewRequest request);

    DtoReviewResponse getReviewById(Long id);

    Page<DtoReviewResponse> getReviewsByProduct(Long productId, Pageable pageable);

    List<DtoReviewResponse> getMyReviews();

    DtoReviewResponse updateReview(Long id, DtoUpdateReviewRequest request);

    void deleteReview(Long id);
}
