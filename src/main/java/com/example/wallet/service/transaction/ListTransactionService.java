package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.ListTransactionRequest;
import com.example.wallet.dto.response.TransactionResponse;
import com.example.wallet.service.BaseService;
import org.springframework.data.domain.Page;

public interface ListTransactionService extends BaseService<ListTransactionRequest, Page<TransactionResponse>> {
}
