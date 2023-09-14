package com.example.wallet.service.account;

import com.example.wallet.dto.request.ListAccountRequest;
import com.example.wallet.dto.response.AccountResponse;
import com.example.wallet.service.BaseService;
import org.springframework.data.domain.Page;

public interface ListAccountService extends BaseService<ListAccountRequest, Page<AccountResponse>> {
}
