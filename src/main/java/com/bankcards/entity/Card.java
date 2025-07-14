package com.bankcards.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    public enum Status {
        NOT_ACTIVE, ACTIVE, BLOCKED, EXPIRED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "card_id")
    private long id;

    @Column(unique = true, name = "card_number", length = 32, nullable = false)
    private String cardNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "card_status", nullable = false)
    private Status status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @JoinColumn(name = "holder_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User holder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "toCard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Collection<Transfer> transfersFrom;

    @OneToMany(mappedBy = "fromCard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Collection<Transfer> transfersTo;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Collection<CardBlockRequest> cardBlockRequests;
}
