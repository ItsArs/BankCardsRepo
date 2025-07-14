package com.bankcards.exception;

public class TransferCardNotActiveException extends RuntimeException {
    public TransferCardNotActiveException(String status) {
        super(status);
    }
}
