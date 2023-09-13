package com.example.wallet.controller;

import com.example.wallet.dto.request.ListTransactionRequest;
import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.dto.request.RegisterRequest;
import com.example.wallet.dto.response.PagingResponse;
import com.example.wallet.dto.response.TransactionResponse;
import com.example.wallet.dto.response.WebResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.service.account.GetLoggedInAccountService;
import com.example.wallet.service.account.RegisterAccountService;
import com.example.wallet.service.transaction.ListTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private RegisterAccountService registerAccountService;
    private GetLoggedInAccountService getLoggedInAccountService;
    private ListTransactionService listTransactionService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterRequest request) throws Exception {
        registerAccountService.execute(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AccountResponse> getLoggedInAccount(Account account) throws Exception {
        AccountResponse accountResponse = getLoggedInAccountService.execute(account);
        return WebResponse.<AccountResponse>builder().data(accountResponse).build();
    }

    @GetMapping(
            path = "/current/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TransactionResponse>> getAccountTransactions(Account account,
                                                         @RequestParam(value = "startTimeEpoch", required = false) Long startTimeEpoch,
                                                         @RequestParam(value = "endTimeEpoch", required = false) Long endTimeEpoch,
                                                         @RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws Exception {
        ListTransactionRequest request = ListTransactionRequest.builder()
                .ownerTransactionAccountNumber(account.getAccountNumber())
                .pageSize(pageSize)
                .currentPage(currentPage)
                .startTimeEpoch(startTimeEpoch)
                .endTimeEpoch(endTimeEpoch)
                .build();

        Page<TransactionResponse> transactionResponses = listTransactionService.execute(request);
        return WebResponse.<List<TransactionResponse>>builder()
                .data(transactionResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(transactionResponses.getNumber())
                        .totalPage(transactionResponses.getTotalPages())
                        .pageSize(transactionResponses.getSize())
                        .build())
                .build();
    }

    //TODO endpoint search account
}
