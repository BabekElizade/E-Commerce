package com.babakalizada.wallet.service;

import com.babakalizada.wallet.dto.request.DtoTopUpRequest;
import com.babakalizada.wallet.dto.response.DtoTopUpResponse;
import com.babakalizada.wallet.dto.response.DtoWalletResponse;
import com.babakalizada.wallet.dto.response.DtoWalletTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IWalletService {
    DtoWalletResponse getMyWallet();

    Page<DtoWalletTransactionResponse> getMyTransactions(
            Pageable pageable
    );

    // Balans artırma prosesini başladır

    DtoTopUpResponse initiateTopUp(
            DtoTopUpRequest request
    );

    // Daxili balans əməliyyatları

    void credit(
            Long walletId,
            BigDecimal amount,
            String referenceId
    );

    void debit(
            Long walletId,
            BigDecimal amount,
            String referenceId
    );
}
