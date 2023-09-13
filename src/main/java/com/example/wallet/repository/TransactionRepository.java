package com.example.wallet.repository;

import com.example.wallet.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findFirstByReferenceNumber(String referenceNumber);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (:startTimeEpoch IS NULL OR t.transactionTimeEpoch >= :startTimeEpoch) " +
            "AND (:endTimeEpoch IS NULL OR t.transactionTimeEpoch <= :endTimeEpoch) " +
            "AND (t.sourceAccount.accountNumber = :ownerAccountNumber OR t.destinationAccount.accountNumber = :ownerAccountNumber)")
    Page<Transaction> findAllByTransactionTimeEpochBetweenAndSourceAccount_AccountNumberOrDestinationAccount_AccountNumber(
            Long startTimeEpoch, Long endTimeEpoch, Long ownerAccountNumber, Pageable pageable);


}
