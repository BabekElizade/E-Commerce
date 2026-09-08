package com.babakalizada.wallet.service.impl;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.InsufficientBalanceException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import com.babakalizada.wallet.dto.request.DtoTopUpRequest;
import com.babakalizada.wallet.dto.response.DtoTopUpResponse;
import com.babakalizada.wallet.dto.response.DtoWalletResponse;
import com.babakalizada.wallet.dto.response.DtoWalletTransactionResponse;
import com.babakalizada.wallet.entity.Wallet;
import com.babakalizada.wallet.entity.WalletTransaction;
import com.babakalizada.wallet.enums.CurrencyType;
import com.babakalizada.wallet.enums.WalletStatus;
import com.babakalizada.wallet.enums.WalletTransactionStatus;
import com.babakalizada.wallet.enums.WalletTransactionType;
import com.babakalizada.wallet.repository.IWalletRepository;
import com.babakalizada.wallet.repository.IWalletTransactionRepository;
import com.babakalizada.wallet.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private final IWalletRepository walletRepository;
    private final IWalletTransactionRepository walletTransactionRepository;
    private final IUserRepository userRepository;

    @Value("${app.wallet.test-top-up-enabled:false}")
    private boolean testTopUpEnabled;

    @Override
    @Transactional
    public DtoWalletResponse getMyWallet() {

        User user = getCurrentUser();
        Wallet wallet = getOrCreateWallet(user);

        return DtoWalletResponse.builder()
                .id(wallet.getId())
                .userId(user.getId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .status(wallet.getStatus())
                .build();
    }

    @Override
    @Transactional
    public Page<DtoWalletTransactionResponse> getMyTransactions(
            Pageable pageable
    ) {

        User user = getCurrentUser();
        Wallet wallet = getOrCreateWallet(user);

        return walletTransactionRepository
                .findByWalletId(wallet.getId(), pageable)
                .map(this::mapTransactionToResponse);
    }

    @Override
    @Transactional
    public DtoTopUpResponse initiateTopUp(DtoTopUpRequest request) {

        if (!testTopUpEnabled) {
            throw new IllegalStateException(
                    "Test top-up is disabled!"
            );
        }

        validateAmount(request.getAmount());

        User user = getCurrentUser();
        Wallet wallet = getOrCreateWallet(user);

        validateWallet(wallet);

        String referenceId = "LOCAL-TOP-UP-" + UUID.randomUUID();

        credit(
                wallet.getId(),
                request.getAmount(),
                referenceId
        );

        return DtoTopUpResponse.builder()
                .paymentId(null)
                .amount(request.getAmount())
                .currency(wallet.getCurrency())
                .transactionType(WalletTransactionType.TOP_UP)
                .build();
    }

    @Override
    @Transactional
    public void credit(
            Long walletId,
            BigDecimal amount,
            String referenceId
    ) {

        validateAmount(amount);
        validateReferenceId(referenceId);

        Wallet wallet = getWalletById(walletId);
        validateWallet(wallet);

        if (walletTransactionRepository.existsByReferenceId(referenceId)) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Wallet Transaction Already Not Exists!"
                    )
            );
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);

        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now());

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .type(WalletTransactionType.TOP_UP)
                .status(WalletTransactionStatus.COMPLETED)
                .amount(amount)
                .balanceAfter(newBalance)
                .referenceId(referenceId)
                .createdAt(LocalDateTime.now())
                .build();

        walletRepository.save(wallet);
        walletTransactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void debit(
            Long walletId,
            BigDecimal amount,
            String referenceId
    ) {

        validateAmount(amount);
        validateReferenceId(referenceId);

        Wallet wallet = getWalletById(walletId);
        validateWallet(wallet);

        if (walletTransactionRepository.existsByReferenceId(referenceId)) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Wallet Transaction Already Not Exists!"
                    )
            );
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    new ErrorMessage(
                            ErrorCode.INSUFFICIENT_BALANCE,
                            "Insufficient Balance!"
                    )

            );
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);

        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now());

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .type(WalletTransactionType.PURCHASE)
                .status(WalletTransactionStatus.COMPLETED)
                .amount(amount)
                .balanceAfter(newBalance)
                .referenceId(referenceId)
                .createdAt(LocalDateTime.now())
                .build();

        walletRepository.save(wallet);
        walletTransactionRepository.save(transaction);
    }

    private Wallet getOrCreateWallet(User user) {

        return walletRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    LocalDateTime now = LocalDateTime.now();

                    Wallet wallet = Wallet.builder()
                            .user(user)
                            .balance(BigDecimal.ZERO)
                            .currency(CurrencyType.USD)
                            .status(WalletStatus.ACTIVE)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                    return walletRepository.save(wallet);
                });
    }

    private Wallet getWalletById(Long walletId) {

        return walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Wallet not found!"
                                )
                        )
                );
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new IllegalStateException(
                    "User is not authenticated!"
            );
        }

        String username = authentication.getName();

        return userRepository.findUsersByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.USER_NOT_FOUND,
                                        "User not found!"
                                )
                        )
                );
    }

    private DtoWalletTransactionResponse mapTransactionToResponse(
            WalletTransaction transaction
    ) {

        return DtoWalletTransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    private void validateWallet(Wallet wallet) {

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Wallet is not active!"
            );
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero!"
            );
        }
    }

    private void validateReferenceId(String referenceId) {

        if (referenceId == null || referenceId.isBlank()) {
            throw new IllegalArgumentException(
                    "Reference ID is required!"
            );
        }
    }
}