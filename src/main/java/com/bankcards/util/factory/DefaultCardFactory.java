package com.bankcards.util.factory;


import com.bankcards.entity.Card;
import com.bankcards.repository.CardRepository;
import com.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DefaultCardFactory implements CardFactory {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public String generateNewCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            double x = Math.random() * 10000;
            long a = Math.round(x);
            if (a < 1000) {
                a += 1000;
            }
            cardNumber.append(a);
            if (i!=3) {
                cardNumber.append("-");
            }
        }
        if (validateCardNumber(String.valueOf(cardNumber))) {
            return cardNumber.toString();
        }
        return generateNewCardNumber();
    }

    @Override
    public boolean validateCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).isEmpty();
    }

    @Override
    public Card generateCard(Long holderId) {
        Card card = new Card();
        card.setHolder(userRepository.findById(holderId).orElseThrow());
        card.setCardNumber(generateNewCardNumber());
        card.setBalance(new BigDecimal(0));
        card.setStatus(Card.Status.NOT_ACTIVE);
        card.setCreatedAt(LocalDateTime.now());
        card.setExpirationDate(LocalDate.now().plusYears(5));
        return card;
    }
}
