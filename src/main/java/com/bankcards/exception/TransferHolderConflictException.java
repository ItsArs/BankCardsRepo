package com.bankcards.exception;

import com.bankcards.entity.Card;

import java.security.Principal;

public class TransferHolderConflictException extends RuntimeException {
    public TransferHolderConflictException(Card card1, Card card2, Principal principal) {
        super(card1.getHolder().getName() + " " + card2.getHolder().getName() + " " + principal.getName());
    }
}
