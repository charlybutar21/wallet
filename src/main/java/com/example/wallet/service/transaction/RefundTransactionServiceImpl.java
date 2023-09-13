package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.RefundRequest;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.TransactionType;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.service.validator.ValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RefundTransactionServiceImpl implements RefundTransactionService {

    private ValidatorService validationService;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Override
    public Long execute(RefundRequest request) throws Exception {

        validationService.validateRequest(request);

        if(referenceNumberIsExist(request.getReferenceNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reference number already inputted");
        }

        Optional<Account> optionalSourceAccount = accountRepository.findById(request.getSourceAccountNumber());
        if(optionalSourceAccount.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source account not found");
        }

        Account sourceAccount = optionalSourceAccount.get();
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Balance");
        }

        Transaction transaction = createTransactionRefund(request, sourceAccount);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transaction.getAmount()));
        accountRepository.save(sourceAccount);

        Account destinationAccount = request.getAccount();
        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(destinationAccount);

        return transaction.getId();

    }

    private Transaction createTransactionRefund(RefundRequest request, Account sourceAccount) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.REFUND);
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(request.getAccount());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionTimeEpoch(System.currentTimeMillis());
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setDescription(request.getDescription());

        transactionRepository.save(transaction);

        return transaction;
    }

    private boolean referenceNumberIsExist(String referenceNumber) {
        Optional<Transaction> transaction = transactionRepository.findFirstByReferenceNumber(referenceNumber);
        return transaction.isPresent();
    }
}
