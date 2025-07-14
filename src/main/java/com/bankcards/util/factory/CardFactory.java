package com.bankcards.util.factory;

import com.bankcards.entity.Card;

public interface CardFactory {
    String generateNewCardNumber();
    boolean validateCardNumber(String cardNumber);
    Card generateCard(Long holderId);
}
