package com.babakalizada.wallet.controller.impl;

import com.babakalizada.wallet.controller.IRestWalletController;
import com.babakalizada.wallet.dto.request.DtoTopUpRequest;
import com.babakalizada.wallet.dto.response.DtoTopUpResponse;
import com.babakalizada.wallet.dto.response.DtoWalletResponse;
import com.babakalizada.wallet.dto.response.DtoWalletTransactionResponse;
import com.babakalizada.wallet.service.IWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class RestWalletControllerImpl implements IRestWalletController {

    private final IWalletService walletService;

    @Override
    @GetMapping("/me/get-my-wallet")
    @ResponseStatus(HttpStatus.OK)
    public DtoWalletResponse getMyWallet() {
        return walletService.getMyWallet();
    }

    @GetMapping("/me/get-my-transactions")
    @ResponseStatus(HttpStatus.OK)
    public Page<DtoWalletTransactionResponse> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return walletService.getMyTransactions(pageable);
    }

    @Override
    @PostMapping("/me/initiate-top-up")
    @ResponseStatus(HttpStatus.CREATED)
    public DtoTopUpResponse initiateTopUp(
            @Valid @RequestBody DtoTopUpRequest request
    ) {
        return walletService.initiateTopUp(request);
    }
}