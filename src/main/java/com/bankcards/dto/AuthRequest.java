package com.bankcards.dto;


import com.bankcards.validation.UniqueUserNameValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotNull(message = "Name can not be null")
    @Size(min = 2, max = 60, message = "Name should be sized between 2 and 60 characters")
    @Pattern(regexp = "[a-zA-Z]{2,60}", message = "You can use only english characters a-z, A-Z")
    @UniqueUserNameValidation
    private String name;

    @NotNull(message = "Password can not be null")
    @Size(min = 4, max = 60, message = "Password should be at least 4 characters and less than 60 characters")
    private String password;
}
