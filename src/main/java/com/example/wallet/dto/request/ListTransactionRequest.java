package com.example.wallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListTransactionRequest {

    private Long ownerTransactionAccountNumber;
    private Long startTimeEpoch;
    private Long endTimeEpoch;
    private Integer currentPage;
    private Integer pageSize;
}
