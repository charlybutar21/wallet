package com.example.wallet.service.account;

import com.example.wallet.dto.request.LoginRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.security.BCrypt;
import com.example.wallet.service.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginAccountServiceImpl implements LoginAccountService {

    private AccountRepository accountRepository;
    private ValidatorService validationService;

    @Override
    @Transactional
    public String execute(LoginRequest request) {
        validationService.validateRequest(request);

        Account user = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            accountRepository.save(user);

            return user.getToken();
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
    }
}
