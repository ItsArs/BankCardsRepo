package com.bankcards.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateRequest {

    @NotNull(message = "Holder id can't be null")
    @Min(value = 0, message = "Invalid holder id")
    private Long holderId;
}
