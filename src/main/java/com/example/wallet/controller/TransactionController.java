package com.example.wallet.controller;

import com.example.wallet.dto.request.RefundRequest;
import com.example.wallet.dto.request.TopUpRequest;
import com.example.wallet.dto.request.TransferRequest;
import com.example.wallet.dto.response.WebResponse;
import com.example.wallet.entity.Account;
import com.example.wallet.service.transaction.RefundTransactionService;
import com.example.wallet.service.transaction.TopUpTransactionService;
import com.example.wallet.service.transaction.TransferTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private TopUpTransactionService topUpTransactionService;
    private TransferTransactionService transferTransactionService;
    private RefundTransactionService refundTransactionService;

    @PostMapping(
            path = "/top-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> topUp(Account account, @RequestBody TopUpRequest request) throws Exception {
        request.setAccount(account);
        topUpTransactionService.execute(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @PostMapping(
            path = "/transfer",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> transfer(Account account, @RequestBody TransferRequest request) throws Exception {
        request.setAccount(account);
        transferTransactionService.execute(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @PostMapping(
            path = "/refund",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> refund(Account account, @RequestBody RefundRequest request) throws Exception {
        request.setAccount(account);
        refundTransactionService.execute(request);
        return WebResponse.<String>builder().data("OK").build();
    }

}
