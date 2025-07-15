package com.bankcards.controller;


import com.bankcards.dto.BlockRequestConclusion;
import com.bankcards.dto.CardBlockRequestDto;
import com.bankcards.dto.CardCreateRequest;
import com.bankcards.dto.CardDto;
import com.bankcards.entity.Card;
import com.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("api/card")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "BearerAuth")
public class CardController {

    private final CardService cardService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllMyCards(@PageableDefault (sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                       Principal principal) {
        return cardService.findAllMyCards(principal, pageable);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> balance(@PathVariable("id") long id) {
        return cardService.getBalance(id);
    }


    @PostMapping("/block-request/create")
    public ResponseEntity<?> requestBlockCard(@Valid @RequestBody CardBlockRequestDto cardBlockRequestDto) {
        return cardService.createBlockRequest(cardBlockRequestDto);
    }

}
