package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.RefundRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.security.BCrypt;
import com.example.wallet.service.validator.ValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefundTransactionServiceTest {
    @Mock
    private ValidatorService validationService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    private RefundTransactionService service;

    private RefundRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new RefundTransactionServiceImpl(validationService, transactionRepository, accountRepository);

        Account tokenAccount = new Account();
        tokenAccount.setAccountNumber(1L);
        tokenAccount.setUsername("username");
        tokenAccount.setAccountHolderName("Test User");
        tokenAccount.setToken("token");
        tokenAccount.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        tokenAccount.setBalance(BigDecimal.valueOf(100000));

        request = new RefundRequest();
        request.setAccount(tokenAccount);
        request.setSourceAccountNumber(2L);
        request.setAmount(new BigDecimal("10000.00"));
        request.setReferenceNumber("REF-003");
        request.setDescription("transfer dari bank");
    }

    @Test
    void testExecute_Success() throws Exception {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber(2L);
        sourceAccount.setBalance(new BigDecimal("90000.00"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber(1L);
        destinationAccount.setBalance(new BigDecimal("110000.00"));

        when(transactionRepository.findFirstByReferenceNumber("REF-003")).thenReturn(Optional.empty());
        when(accountRepository.findById(2L)).thenReturn(Optional.of(sourceAccount));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        sourceAccount.setBalance(new BigDecimal("80000.00"));
        when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount, destinationAccount);

        service.execute(request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void testExecute_ReferenceNumberAlreadyExists() {
        when(transactionRepository.findFirstByReferenceNumber("REF-003")).thenReturn(Optional.of(new Transaction()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Reference number already inputted", exception.getReason());
    }

    @Test
    void testExecute_SourceAccountNotFound() {
        when(transactionRepository.findFirstByReferenceNumber("REF-003")).thenReturn(Optional.empty());
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Source account not found", exception.getReason());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testExecute_InsufficientBalance() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber(2L);
        sourceAccount.setBalance(new BigDecimal("50.00"));

        when(transactionRepository.findFirstByReferenceNumber("REF-003")).thenReturn(Optional.empty());
        when(accountRepository.findById(2L)).thenReturn(Optional.of(sourceAccount));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Insufficient Balance", exception.getReason());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }


}