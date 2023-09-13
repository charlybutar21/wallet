package com.example.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private String type;
    private BigDecimal amount;
    private long transactionTimeEpoch;
    private String referenceNumber;
    private String description;

}
