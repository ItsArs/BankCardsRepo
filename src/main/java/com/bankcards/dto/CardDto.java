package com.bankcards.dto;

import com.bankcards.entity.Card;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @Min(value = 0, message = "Invalid cardId")
    private long id;

    @Pattern(regexp = "[\\d*]{4}[-][\\d*]{4}[-][\\d*]{4}[-][\\d*]{4}", message = "Card number should be in correct format: ****-****-****-****")
    private String cardNumber;

    @Temporal(value = TemporalType.DATE)
    private LocalDate expirationDate;

    @Enumerated
    private Card.Status status;

    @Min(value = 0, message = "Balance can not be less than 0")
    private BigDecimal balance;

    @Min(value = 0, message = "Invalid holder id")
    private long holderId;
}
