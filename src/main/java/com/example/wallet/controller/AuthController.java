package com.example.wallet.controller;

import com.example.wallet.dto.request.LoginRequest;
import com.example.wallet.dto.response.WebResponse;
import com.example.wallet.service.account.LoginAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private LoginAccountService loginAccountService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> login(@RequestBody LoginRequest request) throws Exception {
        String token = loginAccountService.execute(request);
        return WebResponse.<String>builder().data(token).build();
    }
}
