package com.bankcards.controller;


import com.bankcards.dto.AuthRequest;
import com.bankcards.dto.UserDto;
import com.bankcards.security.AuthService;
import com.bankcards.security.JwtUtil;
import com.bankcards.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/token")
    public ResponseEntity<?> authAndGetToken(@RequestBody AuthRequest authRequest) {
        return authService.createToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody AuthRequest authRequest) {
        return userService.create(authRequest);
    }
}
