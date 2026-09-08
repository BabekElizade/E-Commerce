package com.babakalizada.wallet.dto.response;

import com.babakalizada.wallet.enums.CurrencyType;
import com.babakalizada.wallet.enums.WalletStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoWalletResponse {

    private Long id;
    private Long userId;
    private BigDecimal balance;
    private CurrencyType currency;
    private WalletStatus status;
}