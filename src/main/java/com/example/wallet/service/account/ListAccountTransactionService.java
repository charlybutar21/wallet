package com.example.wallet.service.account;

import com.example.wallet.dto.request.ListTransactionRequest;
import com.example.wallet.dto.response.TransactionResponse;
import com.example.wallet.service.BaseService;
import org.springframework.data.domain.Page;

public interface ListAccountTransactionService extends BaseService<ListTransactionRequest, Page<TransactionResponse>> {
}
