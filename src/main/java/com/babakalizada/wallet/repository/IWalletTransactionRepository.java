package com.babakalizada.wallet.repository;

import com.babakalizada.wallet.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Page<WalletTransaction> findByWalletId(
            Long walletId,
            Pageable pageable
    );

    boolean existsByReferenceId(String referenceId);
}
