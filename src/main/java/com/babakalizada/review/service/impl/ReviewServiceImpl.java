package com.babakalizada.review.service.impl;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.ForbiddenException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.product.entity.Product;
import com.babakalizada.product.repository.IProductRepository;
import com.babakalizada.review.dto.request.DtoCreateReviewRequest;
import com.babakalizada.review.dto.request.DtoUpdateReviewRequest;
import com.babakalizada.review.dto.response.DtoReviewResponse;
import com.babakalizada.review.entity.Review;
import com.babakalizada.review.repository.IReviewRepository;
import com.babakalizada.review.service.IReviewService;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReviewServiceImpl implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Transactional
    @Override
    public DtoReviewResponse createReview(DtoCreateReviewRequest request) {
        List<Review> listReview = reviewRepository.findByUserId(getCurrentUser().getId());
        if (!listReview.isEmpty()) {
            for (Review review : listReview) {
                if (review.getProduct().getId().equals(request.getId())) {
                    throw new BusinessException(
                            new ErrorMessage(
                                    ErrorCode.REVIEW_ALREADY_EXISTS,
                                    "Review Already Exist"
                            )
                    );
                }
            }
        }
        if (request == null) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Review resource not found!"
                    )
            );
        }
        User user = getCurrentUser();
        Optional<Product> product = productRepository.findById(request.getId());
        if (product.isEmpty()) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Product resource not found!"
                    )
            );
        }
        Review review = Review.builder()
                .user(user)
                .product(product.get())
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
        DtoReviewResponse response = DtoReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .productId(review.getProduct().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
        return response;
    }

    @Override
    public DtoReviewResponse getReviewById(Long id) {
        if (id < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "ID is not valid!"
                    )
            );
        }
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Review resource not found!"
                    )
            );
        }
        DtoReviewResponse dtoReviewResponse = DtoReviewResponse.builder()
                .id(review.get().getId())
                .userId(review.get().getUser().getId())
                .productId(review.get().getProduct().getId())
                .rating(review.get().getRating())
                .comment(review.get().getComment())
                .build();
        return dtoReviewResponse;
    }

    @Override
    @Transactional
    public Page<DtoReviewResponse> getReviewsByProduct(
            Long productId,
            Pageable pageable
    ) {

        if (productId < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "ID is not valid!"
                    )
            );
        }

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Product not found!"
                    )
            );
        }

        Page<Review> reviews = reviewRepository.findByProductId(
                productId,
                pageable
        );

        return reviews.map(review ->
                DtoReviewResponse.builder()
                        .id(review.getId())
                        .userId(review.getUser().getId())
                        .productId(review.getProduct().getId())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .build()
        );
    }

    @Override
    public Page<DtoReviewResponse> getMyReviews(Pageable pageable) {
        User user = getCurrentUser();
        return reviewRepository.findByUserId(user.getId(), pageable)
                .map(review -> DtoReviewResponse.builder()
                        .id(review.getId())
                        .userId(review.getUser().getId())
                        .productId(review.getProduct().getId())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .build());
    }

    @Override
    @Transactional
    public DtoReviewResponse updateReview(
            Long id,
            DtoUpdateReviewRequest request
    ) {

        if (id < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "ID is not valid!"
                    )
            );
        }

        User currentUser = getCurrentUser();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Review not found!"
                                )
                        )
                );

        // 3. Rəyin sahibi ilə hazırkı istifadəçini müqayisə edirik.
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.ACCESS_DENIED,
                            "You cannot update another user's review!"
                    )
            );
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);

        return DtoReviewResponse.builder()
                .id(updatedReview.getId())
                .userId(updatedReview.getUser().getId())
                .productId(updatedReview.getProduct().getId())
                .rating(updatedReview.getRating())
                .comment(updatedReview.getComment())
                .build();
    }

    @Transactional
    @Override
    public void deleteReview(Long id) {
        if (id < 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "ID is not valid!"
                    )
            );
        }
        User currentUser = getCurrentUser();
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Review resource not found!"
                    )
            );
        }
        boolean isOwner = currentUser.getId().equals(review.get().getUser().getId());
        boolean isAdmin = UserRole.ADMIN.equals(currentUser.getRole());

        if(!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You are not allowed to perform this action!"
                    )
            );
        }
        reviewRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findUsersByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "User Not Found"
                        )
                )
        );
        return user;
    }
}
