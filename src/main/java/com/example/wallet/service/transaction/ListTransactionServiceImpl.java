package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.ListTransactionRequest;
import com.example.wallet.dto.response.TransactionResponse;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListTransactionServiceImpl implements ListTransactionService{

    private final TransactionRepository transactionRepository;

    public ListTransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> execute(ListTransactionRequest request) throws Exception {

        Pageable pageable = PageRequest.of(request.getCurrentPage(), request.getPageSize());
        Page<Transaction> transactionsPage = transactionRepository.findAllByTransactionTimeEpochBetweenAndSourceAccount_AccountNumberOrDestinationAccount_AccountNumber(
                request.getStartTimeEpoch(),
                request.getEndTimeEpoch(),
                request.getOwnerTransactionAccountNumber(),
                pageable);

        List<TransactionResponse> transactionResponses = transactionsPage.getContent().stream()
                .map(this::toTransactionResponse)
                .toList();

        return new PageImpl<>(transactionResponses, pageable, transactionsPage.getTotalElements());
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .type(transaction.getType().toString())
                .amount(transaction.getAmount())
                .transactionTimeEpoch(transaction.getTransactionTimeEpoch())
                .referenceNumber(transaction.getReferenceNumber())
                .description(transaction.getDescription())
                .build();
    }
}
