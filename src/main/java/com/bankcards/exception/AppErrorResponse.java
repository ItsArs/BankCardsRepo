package com.bankcards.exception;


import org.springframework.http.HttpStatus;

import java.util.List;

public record AppErrorResponse (
    String error,
    String body,
    List<String> details
    )
{
    public AppErrorResponse(String error, String body){
        this(error,body,null);
    }
}
