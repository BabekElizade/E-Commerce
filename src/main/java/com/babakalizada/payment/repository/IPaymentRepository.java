package com.babakalizada.payment.repository;

import com.babakalizada.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrder_Id(Long orderId);
    Optional<Payment> findByOrder_Id(Long orderId);
}
