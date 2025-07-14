package com.bankcards.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardBlockRequestDto {

    @Min(value = 0, message = "Invalid CardId")
    @NotNull(message = "CardId can not be null")
    private Long id;

    @NotNull(message = "Name can not be null or empty")
    @Size(min = 4, max = 128, message = "You need to write cause of block request in 4 to 128 characters")
    private String cause;
}
