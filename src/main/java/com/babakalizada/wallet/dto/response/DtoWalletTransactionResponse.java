package com.babakalizada.wallet.dto.response;

import com.babakalizada.wallet.enums.WalletTransactionStatus;
import com.babakalizada.wallet.enums.WalletTransactionType;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoWalletTransactionResponse {

    private Long id;
    private WalletTransactionType type;
    private WalletTransactionStatus status;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;
}
