package com.bankcards.service;


import com.bankcards.dto.TransferRequest;
import com.bankcards.entity.Card;
import com.bankcards.entity.Transfer;
import com.bankcards.exception.CardNotFoundException;
import com.bankcards.exception.TransferCardNotActiveException;
import com.bankcards.exception.TransferHolderConflictException;
import com.bankcards.exception.TransferNotEnoughMoneyException;
import com.bankcards.repository.CardRepository;
import com.bankcards.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final CardService cardService;

    @Transactional
    public ResponseEntity<?> newTransfer(TransferRequest transferDto, Principal principal) {
        Card cardFrom = cardRepository.findById(transferDto.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException(transferDto.getFromCardId()));
        Card cardTo = cardRepository.findById(transferDto.getToCardId()).orElseThrow(() -> new CardNotFoundException(transferDto.getToCardId()));

        if (!cardFrom.getHolder().getName().equals(cardTo.getHolder().getName())
                || !cardFrom.getHolder().getName().equals(principal.getName())) {
            throw new TransferHolderConflictException(cardFrom, cardTo, principal);
        }
        if (cardFrom.getBalance().doubleValue() < transferDto.getAmount().doubleValue()) {
            throw new TransferNotEnoughMoneyException(cardFrom.getBalance().doubleValue() - transferDto.getAmount().doubleValue());
        }
        if (cardFrom.getStatus() != Card.Status.ACTIVE || cardTo.getStatus() != Card.Status.ACTIVE) {
            throw new TransferCardNotActiveException(cardFrom.getStatus().name() + " - " + cardTo.getStatus().name());
        }
        cardFrom.setBalance((cardFrom.getBalance().subtract(transferDto.getAmount())));
        cardTo.setBalance(cardTo.getBalance().subtract(transferDto.getAmount()));

        cardService.updateCardBalance(transferDto.getFromCardId(), cardFrom.getBalance());
        cardService.updateCardBalance(transferDto.getToCardId(), cardTo.getBalance());

        Transfer transfer = new Transfer();
        transfer.setFromCard(cardFrom);
        transfer.setToCard(cardTo);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setHolder(cardFrom.getHolder());
        transfer.setAmount(transferDto.getAmount());
        transferRepository.save(transfer);

        return new ResponseEntity<>("Transfer done!",HttpStatus.OK);
    }
}
