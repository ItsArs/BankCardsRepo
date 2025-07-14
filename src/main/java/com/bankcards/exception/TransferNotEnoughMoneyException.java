package com.bankcards.exception;

import java.math.BigDecimal;

public class TransferNotEnoughMoneyException extends RuntimeException {
    public TransferNotEnoughMoneyException(Double amount) {
        super(amount.toString());
    }
}
