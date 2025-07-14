package com.bankcards.exception;

public class BlockRequestNotFoundException extends RuntimeException {
    public BlockRequestNotFoundException(Long id) {
        super(id.toString());
    }
}
