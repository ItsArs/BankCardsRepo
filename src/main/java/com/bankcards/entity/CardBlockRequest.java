package com.bankcards.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Card_block_requests")
@AllArgsConstructor
@NoArgsConstructor
public class CardBlockRequest {

    public enum BlockRequestStatus {
        PENDING, DECLINED, ACCEPTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "cause")
    private String cause;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "block_request_status", nullable = false)
    private BlockRequestStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CardBlockRequest(Card card, String cause, BlockRequestStatus status, LocalDateTime createdAt) {
        this.card = card;
        this.cause = cause;
        this.status = status;
        this.createdAt = createdAt;
    }
}
