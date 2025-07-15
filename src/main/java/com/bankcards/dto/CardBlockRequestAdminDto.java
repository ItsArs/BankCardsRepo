package com.bankcards.dto;

import com.bankcards.entity.Card;
import com.bankcards.entity.CardBlockRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardBlockRequestAdminDto {

    public enum BlockRequestStatus {
        PENDING, DECLINED, ACCEPTED
    }

    private Long id;

    private long cardId;

    private String cause;

    private CardBlockRequest.BlockRequestStatus status;

    private LocalDateTime createdAt;
}
