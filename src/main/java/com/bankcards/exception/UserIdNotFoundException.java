package com.bankcards.exception;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(Long id) {
        super(id.toString());
    }
}
