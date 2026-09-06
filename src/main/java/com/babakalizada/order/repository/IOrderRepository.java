package com.babakalizada.order.repository;

import com.babakalizada.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Collection<Order> findByUser_IdOrderByCreatedAtDesc(Long id);

    Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);
}
