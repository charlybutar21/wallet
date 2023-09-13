package com.example.wallet.dto.request;

import com.example.wallet.entity.Account;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    @NotNull
    private Account account;

    @NotNull
    @Min(1)
    private Long destinationAccountNumber;

    @NotNull
    @Min(10000)
    private BigDecimal amount;

    @NotBlank @NotNull
    @Size(max = 100)
    private String referenceNumber;

    @Size(max = 200)
    private String description;

}
