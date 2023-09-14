package com.example.wallet.service.account;

import com.example.wallet.dto.request.ListAccountRequest;
import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class ListAccountServiceImpl implements ListAccountService{

    private AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> execute(ListAccountRequest request) throws Exception {
        Pageable pageable = PageRequest.of(request.getCurrentPage(), request.getPageSize());
        Page<Account> accountPage = accountRepository.searchByKeywordAndAccountNumber(
                request.getKeyword(),
                request.getAccountNumber(),
                pageable);

        List<AccountResponse> accountResponses = accountPage.getContent().stream()
                .map(this::toTransactionResponse)
                .toList();

        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

    private AccountResponse toTransactionResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }
}
