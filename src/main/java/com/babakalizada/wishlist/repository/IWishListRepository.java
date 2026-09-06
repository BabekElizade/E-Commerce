package com.babakalizada.wishlist.repository;

import com.babakalizada.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long>
{
    Optional<WishList> findByUserId(Long id);
}
