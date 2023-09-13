package com.example.wallet.service.account;

import com.example.wallet.dto.request.RegisterRequest;
import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.service.validator.ValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class RegisterAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ValidatorService validationService;
    private RegisterAccountService service;
    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new RegisterAccountServiceImpl(accountRepository, validationService);
        request = new RegisterRequest();
    }

    @Test
    void testExecute_Success() throws Exception {
        request.setUsername("username");
        request.setPassword("password");
        request.setAccountHolderName("Test User");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        AccountResponse response = service.execute(request);
        assertNotNull(response);
    }

    @Test
    void testExecute_UsernameAlreadyRegistered() {
        request.setUsername("username");
        request.setPassword("password");
        request.setAccountHolderName("Test User");

        Account existingAccount = new Account();
        existingAccount.setUsername("username");
        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(existingAccount));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username already registered", exception.getReason());
    }
}