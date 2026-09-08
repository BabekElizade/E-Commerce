package com.babakalizada.wallet.controller;

import com.babakalizada.wallet.dto.request.DtoTopUpRequest;
import com.babakalizada.wallet.dto.response.DtoTopUpResponse;
import com.babakalizada.wallet.dto.response.DtoWalletResponse;
import com.babakalizada.wallet.dto.response.DtoWalletTransactionResponse;
import org.springframework.data.domain.Page;

public interface IRestWalletController {
    DtoWalletResponse getMyWallet();

    Page<DtoWalletTransactionResponse> getMyTransactions(
            int page,
            int size
    );

    DtoTopUpResponse initiateTopUp(
            DtoTopUpRequest request
    );
}
