package com.bankcards.controller;


import com.bankcards.dto.BlockRequestConclusion;
import com.bankcards.dto.CardBlockRequestDto;
import com.bankcards.dto.CardCreateRequest;
import com.bankcards.dto.CardDto;
import com.bankcards.entity.Card;
import com.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Patch;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public HttpEntity<?> getAllMyCards(@PageableDefault (sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                       Principal principal) {
        return cardService.findAllMyCards(principal, pageable);
    }

    @GetMapping("/all/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<Page<CardDto>> getAllCards(@PageableDefault (sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return cardService.findAll(pageable);
    }

    @GetMapping("/balance/{id}")
    @PreAuthorize("hasRole('USER')")
    public HttpEntity<BigDecimal> balance(@PathVariable("id") long id) {
        return cardService.getBalance(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewCard(@Valid @RequestBody CardCreateRequest cardCreateRequest) {
        return cardService.createNewCard(cardCreateRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCard(@PathVariable("id") long id,@Valid @RequestBody CardDto cardDto) {
        return cardService.updateCard(id, cardDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCard(@PathVariable("id") long id) {
        return cardService.deleteCard(id);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateCard(@PathVariable("id") long id) {
        return cardService.updateCardStatus(id, Card.Status.ACTIVE);
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockCard(@PathVariable("id") long id) {
        return cardService.updateCardStatus(id, Card.Status.BLOCKED);
    }

    @GetMapping("/block-request/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAllBlockRequests(@PageableDefault Pageable pageable) {
        return cardService.findAllBlockRequests(pageable);
    }

    @GetMapping("/block-request/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAllPendingBlockRequests(@PageableDefault Pageable pageable) {
        return cardService.findAllPendingBlockRequests(pageable);
    }

    @PatchMapping("/block-request/response")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockRequestConclusion(@RequestBody BlockRequestConclusion blockRequestConclusion) {
        return cardService.closeBlockRequest(blockRequestConclusion);
    }

    @PostMapping("/block-request/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestBlockCard(@Valid @RequestBody CardBlockRequestDto cardBlockRequestDto) {
        return cardService.createBlockRequest(cardBlockRequestDto);
    }

}
