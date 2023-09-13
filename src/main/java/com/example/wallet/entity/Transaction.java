package com.example.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "source_account_id", referencedColumnName = "account_number")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", referencedColumnName = "account_number")
    private Account destinationAccount;

    private BigDecimal amount;

    @Column(name = "transaction_time_epoch")
    private long transactionTimeEpoch;

    @Column(name = "reference_number", unique = true, nullable = false, length = 100)
    private String referenceNumber;

    private String description;

}
