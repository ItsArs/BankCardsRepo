package com.bankcards.exception;


import com.bankcards.entity.Role;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.Enumerated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppErrorResponse> handleMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        AppErrorResponse errorResponse = new AppErrorResponse(
                "Validation Error!",
                "Check details!",
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleUsernameNotFoundError(UsernameNotFoundException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "User with this name does not exist!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleUserIdNotFoundError(UserIdNotFoundException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "User with this id does not exist!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleCardNotFoundError(CardNotFoundException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Card with this id does not exist!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardStatusConflictException.class)
    public ResponseEntity<AppErrorResponse> handleCardStatusConflictError(CardStatusConflictException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "This card status is already set!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleRoleNotFoundError(RoleNotFoundException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Required role not found error!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransferCardNotActiveException.class)
    public ResponseEntity<AppErrorResponse> handleTransferCardNotActiveError(TransferCardNotActiveException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Some of cards are not active!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(TransferHolderConflictException.class)
    public ResponseEntity<AppErrorResponse> handleTransferHolderConflictError(TransferHolderConflictException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "You can do transfer between only your own cards",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(TransferNotEnoughMoneyException.class)
    public ResponseEntity<AppErrorResponse> handleTransferNotEnoughMoneyException(TransferNotEnoughMoneyException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Not enough money! You need more:",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockRequestNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleBlockRequestNotFoundError(BlockRequestNotFoundException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Block request not found!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppErrorResponse> badCredentialsError(BadCredentialsException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Bad credentials!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AppErrorResponse> handleAuthError(AuthenticationException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Authentication error!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<AppErrorResponse> handleJwtError(JwtException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Jwt bearer error!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppErrorResponse> handleIllegalArgumentError(IllegalArgumentException e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Illegal argument received!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handleAllException(Exception e) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                "Something went wrong!",
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
