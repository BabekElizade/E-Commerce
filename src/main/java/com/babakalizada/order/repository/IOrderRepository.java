package com.babakalizada.order.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser_IdOrderByCreatedAtDesc(Long id, Pageable pageable);

    Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);
}
