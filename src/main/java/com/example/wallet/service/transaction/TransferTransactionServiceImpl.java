package com.example.wallet.service.transaction;

import com.example.wallet.dto.request.TransferRequest;
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
public class TransferTransactionServiceImpl implements TransferTransactionService {

    private ValidatorService validationService;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;


    @Override
    @Transactional
    public Long execute(TransferRequest request) throws Exception {
        validationService.validateRequest(request);

        if(referenceNumberIsExist(request.getReferenceNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reference number already inputted");
        }

        if (request.getAccount().getBalance().compareTo(request.getAmount()) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Balance");
        }

        Optional<Account> optionalDestinationAccount = accountRepository.findById(request.getDestinationAccountNumber());
        if(optionalDestinationAccount.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination account not found");
        }

        Account destinationAccount = optionalDestinationAccount.get();
        Transaction transaction = createTransactionTransfer(request, destinationAccount);

        Account sourceAccount = request.getAccount();
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transaction.getAmount()));
        accountRepository.save(sourceAccount);

        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(destinationAccount);

        return transaction.getId();
    }

    private Transaction createTransactionTransfer(TransferRequest request, Account destinationAccount) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(request.getAccount());
        transaction.setDestinationAccount(destinationAccount);
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
