package com.example.wallet.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank @NotNull
    @Size(max = 100)
    private String username;

    @NotBlank @NotNull
    @Size(max = 100)
    private String password;

    @NotBlank @NotNull
    @Size(max = 100)
    private String accountHolderName;
}
