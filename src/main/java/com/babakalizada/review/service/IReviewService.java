package com.babakalizada.review.service;

import com.babakalizada.review.dto.request.DtoCreateReviewRequest;
import com.babakalizada.review.dto.request.DtoUpdateReviewRequest;
import com.babakalizada.review.dto.response.DtoReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IReviewService {
    DtoReviewResponse createReview(DtoCreateReviewRequest request);

    DtoReviewResponse getReviewById(Long id);

    Page<DtoReviewResponse> getReviewsByProduct(Long productId, Pageable pageable);

    Page<DtoReviewResponse> getMyReviews(Pageable pageable);

    DtoReviewResponse updateReview(Long id, DtoUpdateReviewRequest request);

    void deleteReview(Long id);
}
