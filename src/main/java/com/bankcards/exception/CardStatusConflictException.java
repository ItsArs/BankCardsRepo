package com.bankcards.exception;

public class CardStatusConflictException extends RuntimeException {
    public CardStatusConflictException(String message) {
        super(message);
    }
}
