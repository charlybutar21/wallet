package com.example.wallet.service.account;

import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetLoggedInAccountServiceTest {

    private GetLoggedInAccountService service;

    @BeforeEach
    void setUp() {
        service = new GetLoggedInAccountServiceImpl();
    }

    @Test
    void execute() throws Exception {

        Account account = new Account();
        account.setAccountNumber(1L);
        account.setUsername("username");
        account.setAccountHolderName("Test User");
        account.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        account.setBalance(BigDecimal.ZERO);
        AccountResponse accountResponse = service.execute(account);
        assertNotNull(accountResponse);
        assertEquals(1L, accountResponse.getAccountNumber());
    }
}