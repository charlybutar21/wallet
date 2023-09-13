package com.example.wallet.service.account;

import com.example.wallet.dto.request.LoginRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.security.BCrypt;
import com.example.wallet.service.validator.ValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LoginAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ValidatorService validationService;

    private LoginAccountService service;
    private LoginRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new LoginAccountServiceImpl(accountRepository, validationService);
        request = new LoginRequest();
    }

    @Test
    void testExecute_Success() throws Exception {
        request.setUsername("username");
        request.setPassword("password");

        Account user = new Account();
        user.setUsername("username");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(user));

        String token = service.execute(request);

        assertNotNull(token);
    }

    @Test
    void testExecute_InvalidUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        when(accountRepository.findByUsername("username")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Username or password wrong", exception.getReason());
    }

    @Test
    void testExecute_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("wrong-password");

        Account user = new Account();
        user.setUsername("username");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        when(accountRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.execute(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Username or password wrong", exception.getReason());
    }

}