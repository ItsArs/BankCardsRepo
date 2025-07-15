package com.bankcards.controller;

import com.bankcards.dto.UserDto;
import com.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;


    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PatchMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        return userService.update( userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        return userService.delete(id);
    }

}
