package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.ListTransactionRequest;
import com.example.wallet.dto.response.TransactionResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.TransactionType;
import com.example.wallet.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ListTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private ListTransactionService service;

    private ListTransactionRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ListTransactionServiceImpl(transactionRepository);
        request= new ListTransactionRequest();
        request.setPageSize(10);
        request.setCurrentPage(0);
    }


    @Test
    void testExecute_Success() throws Exception {
        List<Transaction> mockTransactions = createMockTransactions();
        Page<Transaction> mockTransactionsPage = new PageImpl<>(mockTransactions);

        when(transactionRepository.findAllByTransactionTimeEpochBetweenAndSourceAccount_AccountNumberOrDestinationAccount_AccountNumber(
                request.getStartTimeEpoch(),
                request.getEndTimeEpoch(),
                request.getOwnerTransactionAccountNumber(),
                PageRequest.of(request.getCurrentPage(), request.getPageSize())
        )).thenReturn(mockTransactionsPage);

        Page<TransactionResponse> responsePage = service.execute(request);

        assertNotNull(responsePage);
        assertEquals(3, responsePage.getTotalElements());
        assertEquals(1, responsePage.getTotalPages());
        assertEquals(0, responsePage.getNumber());
        assertEquals(10, responsePage.getSize());
    }

    private List<Transaction> createMockTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber(1L);

        Account destinationAccount = new Account();
        sourceAccount.setAccountNumber(2L);

        Transaction transaction1 = new Transaction();
        transaction1.setType(TransactionType.TOP_UP);
        transaction1.setAmount(new BigDecimal("100000.00"));
        transaction1.setDestinationAccount(destinationAccount);
        transaction1.setTransactionTimeEpoch(1631642400000L);
        transaction1.setReferenceNumber("REF-001");
        transaction1.setDescription("note");
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setType(TransactionType.TRANSFER);
        transaction2.setAmount(new BigDecimal("10000.00"));
        transaction2.setSourceAccount(sourceAccount);
        transaction2.setDestinationAccount(destinationAccount);
        transaction2.setTransactionTimeEpoch(1631642410000L);
        transaction2.setReferenceNumber("REF-002");
        transaction2.setDescription("note");
        transactions.add(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setType(TransactionType.REFUND);
        transaction3.setAmount(new BigDecimal("200.00"));
        transaction3.setSourceAccount(sourceAccount);
        transaction3.setDestinationAccount(destinationAccount);
        transaction3.setTransactionTimeEpoch(1631642420000L);
        transaction3.setReferenceNumber("REF-003");
        transaction3.setDescription("note");
        transactions.add(transaction3);

        return transactions;
    }
}