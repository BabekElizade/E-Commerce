package com.babakalizada.wishlist.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.wishlist.entity.WishListItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWishItemsRepository extends JpaRepository<WishListItems, Long> {
    Page<WishListItems> findByWishlist_Id(Long wishlistId, Pageable pageable);

    Optional<WishListItems> findByWishlist_IdAndProduct_Id(
            Long wishlistId,
            Long productId
    );
}
