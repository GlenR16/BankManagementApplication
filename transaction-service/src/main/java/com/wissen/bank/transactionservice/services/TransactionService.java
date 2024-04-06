package com.wissen.bank.transactionservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.exceptions.InvalidDataException;
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

    public List<Transaction> getTransactionsByAccountNumber(long accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction, Status status ){
        if (transaction == null || !validateTransaction(transaction)) {
            throw new InvalidDataException("Invalid Transaction");
        }
        Transaction _transaction ;
        if (transaction.getTypeId() == 1) {
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .beneficiaryId(transaction.getBeneficiaryId())
                .amount(transaction.getAmount())
                .typeId(1)
                .status(status)
                .build();
        } else if (transaction.getTypeId() == 2 || transaction.getTypeId() == 3){
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .typeId(transaction.getTypeId())
                .status(status)
                .build();
        } else if (transaction.getTypeId() == 4) {
            _transaction = Transaction
                .builder()
                .accountNumber(transaction.getAccountNumber())
                .cardNumber(transaction.getCardNumber())
                .beneficiaryId(transaction.getBeneficiaryId())
                .amount(transaction.getAmount())
                .typeId(4)
                .status(status)
                .build();
        }
        else{
            throw new InvalidDataException("Invalid Transaction");
        }
        return transactionRepository.save(_transaction);
    }

    public boolean validateTransaction(Transaction transaction) {
        if (transaction.getAmount() <= 0 || transaction.getTypeId() <= 0)
            return false;
        else if (transaction.getTypeId() == 1 && ( transaction.getBeneficiaryId() == 0 || transaction.getAccountNumber() == 0 ))
            return false;
        else if ((transaction.getTypeId() == 2 || transaction.getTypeId() == 3 ) && transaction.getAccountNumber() == 0) 
            return false;
        else if (transaction.getTypeId() == 4 && ( transaction.getAccountNumber() == 0 || transaction.getCardNumber() == 0 || transaction.getBeneficiaryId() == 0))
            return false;
        return true;
    }
}
