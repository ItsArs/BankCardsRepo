package com.bankcards.dto;

import com.bankcards.entity.CardBlockRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockRequestConclusion {

    @NotNull(message = "Invalid blockRequestId")
    @Min(value = 0, message = "Invalid blockRequestId")
    private Long blockRequestId;

    @NotNull(message = "Invalid status")
    private CardBlockRequest.BlockRequestStatus status;
}
