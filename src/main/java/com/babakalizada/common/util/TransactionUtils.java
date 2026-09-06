package com.babakalizada.common.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TransactionUtils {
    public static String transactionalId(){
        return UUID.randomUUID().toString();
    }
}
