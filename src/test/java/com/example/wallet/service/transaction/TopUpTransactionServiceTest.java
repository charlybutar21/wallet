package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.TopUpRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TopUpTransactionServiceTest {

    @Mock
    private ValidatorService validationService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    private TopUpTransactionService service;

    private TopUpRequest request;

    private Account tokenAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TopUpTransactionServiceImpl(validationService, transactionRepository, accountRepository);

        tokenAccount = new Account();
        tokenAccount.setAccountNumber(1L);
        tokenAccount.setUsername("username");
        tokenAccount.setAccountHolderName("Test User");
        tokenAccount.setToken("token");
        tokenAccount.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        tokenAccount.setBalance(BigDecimal.ZERO);

        request = new TopUpRequest();
        request.setAccount(tokenAccount);
        request.setAmount(new BigDecimal("100000.00"));
        request.setReferenceNumber("REF-001");
        request.setDescription("transfer dari bank");
    }

    @Test
    void testExecute_Success() throws Exception {
        when(transactionRepository.findFirstByReferenceNumber("REF-001")).thenReturn(Optional.empty());
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(accountRepository.save(any(Account.class))).thenReturn(tokenAccount);

        service.execute(request);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testExecute_ReferenceNumberAlreadyExists() {
        when(transactionRepository.findFirstByReferenceNumber("REF-001")).thenReturn(Optional.of(new Transaction()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Reference number already inputted", exception.getReason());
    }
}