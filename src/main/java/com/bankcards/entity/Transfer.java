package com.bankcards.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transfers")
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private long id;

    @JoinColumn(name = "card_from_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Card fromCard;

    @JoinColumn(name = "card_to_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Card toCard;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @JoinColumn(name = "holder_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User holder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
