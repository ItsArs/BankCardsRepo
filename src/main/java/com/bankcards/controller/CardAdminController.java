package com.bankcards.controller;


import com.bankcards.dto.BlockRequestConclusion;
import com.bankcards.dto.CardCreateRequest;
import com.bankcards.dto.CardDto;
import com.bankcards.entity.Card;
import com.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/card")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "BearerAuth")
public class CardAdminController {
    private final CardService cardService;

    @GetMapping("/all")
    public ResponseEntity<Page<CardDto>> getAllCards(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return cardService.findAll(pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewCard(@Valid @RequestBody CardCreateRequest cardCreateRequest) {
        return cardService.createNewCard(cardCreateRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable("id") long id,@Valid @RequestBody CardDto cardDto) {
        return cardService.updateCard(id, cardDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable("id") long id) {
        return cardService.deleteCard(id);
    }


    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateCard(@PathVariable("id") long id) {
        return cardService.updateCardStatus(id, Card.Status.ACTIVE);
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<?> blockCard(@PathVariable("id") long id) {
        return cardService.updateCardStatus(id, Card.Status.BLOCKED);
    }

    @GetMapping("/block-request/all")
    public ResponseEntity<?> findAllBlockRequests(@PageableDefault Pageable pageable) {
        return cardService.findAllBlockRequests(pageable);
    }

    @GetMapping("/block-request/pending")
    public ResponseEntity<?> findAllPendingBlockRequests(@PageableDefault Pageable pageable) {
        return cardService.findAllPendingBlockRequests(pageable);
    }

    @PatchMapping("/block-request/response")
    public ResponseEntity<?> blockRequestConclusion(@RequestBody BlockRequestConclusion blockRequestConclusion) {
        return cardService.closeBlockRequest(blockRequestConclusion);
    }

}
