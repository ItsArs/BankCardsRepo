package com.bankcards.controller;


import com.bankcards.dto.TransferRequest;
import com.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class TransferController {
    private final TransferService transferService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferDto, Principal principal) {
        return transferService.newTransfer(transferDto, principal);
    }
}
