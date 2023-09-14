package com.example.wallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListAccountRequest {

    private String keyword;
    private Long accountNumber;
    private Integer currentPage;
    private Integer pageSize;
}
