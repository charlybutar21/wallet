package com.example.wallet.service.account;

import com.example.wallet.dto.request.ListAccountRequest;
import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.security.BCrypt;
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

class ListAccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    private ListAccountService service;

    private ListAccountRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ListAccountServiceImpl(accountRepository);
        request= new ListAccountRequest();
        request.setPageSize(10);
        request.setCurrentPage(0);
    }

    @Test
    void testExecute_Success() throws Exception {
        List<Account> mockAccounts = createMockAccounts();
        Page<Account> mockTransactionsPage = new PageImpl<>(mockAccounts);

        when(accountRepository.searchByKeywordAndAccountNumber(
                request.getKeyword(),
                request.getAccountNumber(),
                PageRequest.of(request.getCurrentPage(), request.getPageSize())
        )).thenReturn(mockTransactionsPage);

        Page<AccountResponse> responsePage = service.execute(request);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(1, responsePage.getTotalPages());
        assertEquals(0, responsePage.getNumber());
        assertEquals(10, responsePage.getSize());
    }

    private List<Account> createMockAccounts() {
        List<Account> accounts = new ArrayList<>();

        Account account = new Account();
        account.setAccountNumber(1L);
        account.setAccountHolderName("Test User");
        account.setBalance(BigDecimal.ZERO);
        account.setUsername("username");
        account.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        accounts.add(account);

        return accounts;
    }
}