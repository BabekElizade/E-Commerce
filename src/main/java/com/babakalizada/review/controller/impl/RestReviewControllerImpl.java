package com.babakalizada.review.controller.impl;

import com.babakalizada.review.controller.IRestReviewController;
import com.babakalizada.review.dto.request.DtoCreateReviewRequest;
import com.babakalizada.review.dto.request.DtoUpdateReviewRequest;
import com.babakalizada.review.dto.response.DtoReviewResponse;
import com.babakalizada.review.service.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class RestReviewControllerImpl implements IRestReviewController {

    private final IReviewService reviewService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DtoReviewResponse createReview(
            @Valid @RequestBody DtoCreateReviewRequest request
    ) {
        return reviewService.createReview(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoReviewResponse getReviewById(
            @PathVariable("id") Long id
    ) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/get-by-product-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public Page<DtoReviewResponse> getReviewsByProduct(
            @PathVariable("id") Long productId,
            Pageable pageable
    ) {
        return reviewService.getReviewsByProduct(productId, pageable);
    }

    @GetMapping("/get-my-reviews")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<DtoReviewResponse> getMyReviews() {
        return reviewService.getMyReviews();
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DtoReviewResponse updateReview(
            @PathVariable("id") Long id,
            @Valid @RequestBody DtoUpdateReviewRequest request
    ) {
        return reviewService.updateReview(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteReview(
            @PathVariable("id") Long id
    ) {
        reviewService.deleteReview(id);
    }
}