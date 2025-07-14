package com.bankcards.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest{

    @NotNull(message = "Card id can't be null or empty")
    @Min(value = 0, message = "Invalid fromCard id")
    private long fromCardId;

    @NotNull(message = "Card id can't be null or empty")
    @Min(value = 0, message = "Invalid toCard id")
    private long toCardId;

    @NotNull(message = "Amount can't be null or empty")
    @Min(value = 0, message = "U can transfer only positive numbers")
    private BigDecimal amount;
}
