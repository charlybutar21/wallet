package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.TopUpRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.TransactionType;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.service.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TopUpTransactionServiceImpl implements TopUpTransactionService {

    private ValidatorService validationService;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;


    @Override
    @Transactional
    public Long execute(TopUpRequest request) throws Exception {
        validationService.validateRequest(request);

        if(referenceNumberIsExist(request.getReferenceNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reference number already inputted");
        }

        Transaction transaction = createTransactionTopUp(request);

        Account account = request.getAccount();
        account.setBalance(account.getBalance().add(transaction.getAmount()));
        accountRepository.save(account);

        return transaction.getId();
    }

    private boolean referenceNumberIsExist(String referenceNumber) {
        Optional<Transaction> transaction = transactionRepository.findFirstByReferenceNumber(referenceNumber);
        return transaction.isPresent();
    }

    private Transaction createTransactionTopUp(TopUpRequest request) {
        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber(request.getAccount().getAccountNumber());

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TOP_UP);
        transaction.setAmount(request.getAmount());
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionTimeEpoch(System.currentTimeMillis());
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setDescription(request.getDescription());

        transactionRepository.save(transaction);

        return transaction;
    }

}
