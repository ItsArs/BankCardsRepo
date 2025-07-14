package com.bankcards.service;


import com.bankcards.dto.BlockRequestConclusion;
import com.bankcards.dto.CardBlockRequestDto;
import com.bankcards.dto.CardCreateRequest;
import com.bankcards.dto.CardDto;
import com.bankcards.entity.Card;
import com.bankcards.entity.CardBlockRequest;
import com.bankcards.exception.BlockRequestNotFoundException;
import com.bankcards.exception.CardNotFoundException;
import com.bankcards.exception.CardStatusConflictException;
import com.bankcards.repository.CardBlockRequestRepository;
import com.bankcards.repository.CardRepository;
import com.bankcards.repository.UserRepository;
import com.bankcards.util.factory.CardFactory;
import com.bankcards.util.factory.DefaultCardFactory;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DefaultCardFactory cardFactory;
    private final CardBlockRequestRepository blockRequestRepository;

    public ResponseEntity<Page<CardDto>> findAll(Pageable pageable) {
        return new ResponseEntity<>(cardRepository.findAll(pageable)
                .map(c -> modelMapper.map(c, CardDto.class))
                .map(this::maskCardNumberOnDto), HttpStatus.OK);
    }

    public ResponseEntity<?> findAllMyCards(Principal principal, Pageable pageable) {
        long id = userRepository.findByName(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName())).getId();

        return new ResponseEntity<>(cardRepository.findAllByHolderId(id, pageable)
                .map(c -> modelMapper.map(c, CardDto.class))
                .map(this::maskCardNumberOnDto)
                .toList(), HttpStatus.OK);
    }

    public CardDto maskCardNumberOnDto(CardDto cardDto) {
        String cardNumber = cardDto.getCardNumber();
        cardDto.setCardNumber("****-****-****-" + cardNumber.substring(cardNumber.length()-4));
        return cardDto;
    }

    public ResponseEntity<BigDecimal> getBalance(long id) {
        return cardRepository.findById(id).map((card -> new ResponseEntity<>(card.getBalance(), HttpStatus.OK)))
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    @Transactional
    public ResponseEntity<?> updateCard(long id, CardDto cardDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        modelMapper.map(cardDto, card);
        cardRepository.save(card);
        return new ResponseEntity<>("Card has been updated",HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateCardBalance(long id, BigDecimal balance) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        card.setBalance(balance);
        cardRepository.save(card);
        return new ResponseEntity<>("Card's balance has been updated",HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createNewCard(CardCreateRequest cardCreateRequest) {
        Card card = cardFactory.generateCard(cardCreateRequest.getHolderId());
        cardRepository.save(card);
        return new ResponseEntity<>(maskCardNumberOnDto(modelMapper.map(card, CardDto.class)), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteCard(long id) {
        cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        cardRepository.deleteById(id);
        return new ResponseEntity<>("Card has been deleted",HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateCardStatus(long id, Card.Status status) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        if (card.getStatus().equals(status)){
            throw new CardStatusConflictException(status.toString());
        }

        card.setStatus(status);
        cardRepository.save(card);
        return new ResponseEntity<>("Status has been set fine!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createBlockRequest(CardBlockRequestDto cardBlockRequestDto) {
        Card card = cardRepository.findById(cardBlockRequestDto.getId()).orElseThrow(
                () -> new CardNotFoundException(cardBlockRequestDto.getId())
        );
        CardBlockRequest cardBlockRequest = new CardBlockRequest(
                card, cardBlockRequestDto.getCause(), CardBlockRequest.BlockRequestStatus.PENDING, LocalDateTime.now()
        );
        blockRequestRepository.save(cardBlockRequest);
        return new ResponseEntity<>("Card block request created!", HttpStatus.CREATED);
    }

    public ResponseEntity<Page<CardBlockRequest>> findAllBlockRequests(Pageable pageable) {
        return new ResponseEntity<>(blockRequestRepository.findAll(pageable), HttpStatus.OK);
    }

    public ResponseEntity<?> findAllPendingBlockRequests(Pageable pageable) {
        return new ResponseEntity<>(blockRequestRepository.findAllByStatusEquals(CardBlockRequest.BlockRequestStatus.PENDING, pageable), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> closeBlockRequest(BlockRequestConclusion blockRequestConclusion){
        CardBlockRequest cardBlockRequest = blockRequestRepository.findById(blockRequestConclusion.getBlockRequestId())
                .orElseThrow(() -> new BlockRequestNotFoundException(blockRequestConclusion.getBlockRequestId()));
        Card card = cardRepository.findById(cardBlockRequest.getCard().getId())
                .orElseThrow(() -> new CardNotFoundException(cardBlockRequest.getCard().getId()));

        switch (blockRequestConclusion.getStatus()){
            case DECLINED:
                cardBlockRequest.setStatus(CardBlockRequest.BlockRequestStatus.DECLINED);
                blockRequestRepository.save(cardBlockRequest);
                return new ResponseEntity<>("Block request has been declined", HttpStatus.OK);
            case ACCEPTED:
                cardBlockRequest.setStatus(CardBlockRequest.BlockRequestStatus.ACCEPTED);
                card.setStatus(Card.Status.BLOCKED);
                cardRepository.save(card);
                return new ResponseEntity<>("Card has been blocked", HttpStatus.OK);
            default:
                return new ResponseEntity<>("Nothing been done. Block request hasn't been closed.", HttpStatus.BAD_REQUEST);
        }
    }
}
