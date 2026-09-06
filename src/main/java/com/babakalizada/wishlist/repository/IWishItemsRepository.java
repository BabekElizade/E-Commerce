package com.babakalizada.wishlist.repository;

import com.babakalizada.wishlist.entity.WishListItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWishItemsRepository extends JpaRepository<WishListItems, Long> {
    Optional<WishListItems> findByWishlist_IdAndProduct_Id(
            Long wishlistId,
            Long productId
    );}
