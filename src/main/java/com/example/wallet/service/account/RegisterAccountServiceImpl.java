package com.example.wallet.service.account;

import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.dto.request.RegisterRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.security.BCrypt;
import com.example.wallet.service.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterAccountServiceImpl implements RegisterAccountService {

    private static final long MIN_ACCOUNT_NUMBER = 1000000000L; // 10-digit minimum
    private static final long MAX_ACCOUNT_NUMBER = 9999999999L; // 10-digit maximum

    private AccountRepository accountRepository;
    private ValidatorService validationService;

    @Override
    @Transactional
    public AccountResponse execute(RegisterRequest request) {
        validationService.validateRequest(request);

        if (isExistByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        return createAccount(request);
    }

    private boolean isExistByUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.isPresent();
    }

    private AccountResponse createAccount(RegisterRequest request) {
        Account account = new Account();
        long accountNumber = MIN_ACCOUNT_NUMBER + (long) (Math.random() * (MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1));
        account.setAccountNumber(accountNumber);
        account.setUsername(request.getUsername());
        account.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }
}
