package com.example.wallet.service.account;

import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.entity.Account;
import org.springframework.stereotype.Service;

@Service
public class GetLoggedInAccountServiceImpl implements GetLoggedInAccountService {

    @Override
    public AccountResponse execute(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }

}
