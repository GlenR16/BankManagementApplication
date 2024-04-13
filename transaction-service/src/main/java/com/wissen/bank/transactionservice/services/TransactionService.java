package com.wissen.bank.transactionservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.transactionservice.models.Status;
import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.repositories.TransactionRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getTransactionByTransactionId(long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    public Page<Transaction> getTransactionsByAccountNumber(long accountNumber) {
        return transactionRepository.findByAccountNumberOrderByCreatedAtDesc(accountNumber, PageRequest.of(0, 100));
    }

    public Page<Transaction> getTransactionsByAccountNumber(long accountNumber, int page) {
        return transactionRepository.findByAccountNumberOrderByCreatedAtDesc(accountNumber, PageRequest.of(page, 6));
    }

    public Page<Transaction> getAllTransactions(int page) {
        return transactionRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 6));
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction, Status status ){
        if (transaction == null || !validateTransaction(transaction)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction details provided.");
        Transaction _transaction ;
        if (transaction.getTypeId() == 1) {
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .beneficiaryId(transaction.getBeneficiaryId())
                .balance(transaction.getBalance())
                .credit(transaction.getCredit())
                .debit(transaction.getDebit())
                .typeId(1)
                .status(status)
                .build();
        } else if (transaction.getTypeId() == 2 || transaction.getTypeId() == 3){
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .credit(transaction.getCredit())
                .balance(transaction.getBalance())
                .debit(transaction.getDebit())
                .typeId(transaction.getTypeId())
                .status(status)
                .build();
        } else if (transaction.getTypeId() == 4) {
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .cardNumber(transaction.getCardNumber())
                .beneficiaryId(transaction.getBeneficiaryId())
                .credit(transaction.getCredit())
                .balance(transaction.getBalance())
                .debit(transaction.getDebit())
                .typeId(4)
                .status(status)
                .build();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction type provided.");
        }
        return transactionRepository.save(_transaction);
    }

    public boolean validateTransaction(Transaction transaction) {
        if (transaction.getTypeId() <= 0 || transaction.getAccountNumber() <= 0 || ( transaction.getCredit() <= 0 && transaction.getDebit() <= 0 ) || transaction.getDebit() < 0 || transaction.getCredit() < 0){
            System.out.println("Error on type 0");
            return false;
        }
        else if (transaction.getTypeId() == 1 && ( transaction.getDebit() > 0 && transaction.getBeneficiaryId() == 0 )){
            System.out.println("Error on type 1");
            return false;
        }
        else if (transaction.getTypeId() == 4 && ( transaction.getDebit() > 0 && (transaction.getCardNumber() == 0 || transaction.getBeneficiaryId() == 0))){
            System.out.println("Error on type 2");
            return false;
        }
        return true;
    }
}
