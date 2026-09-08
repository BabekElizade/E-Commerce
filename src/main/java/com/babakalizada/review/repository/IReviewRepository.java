package com.babakalizada.review.repository;

import com.babakalizada.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductId(Long userId, Pageable pageable);
    List<Review> findByUserId(Long userId);
}
