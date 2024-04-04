package com.wissen.bank.transactionservice.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.exceptions.InvalidDataException;
import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.repositories.TransactionRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public Transaction getTransactionByTransactionId(long id)
    {
        return transactionRepository.findById(id).orElseThrow(()-> new NotFoundException("Transaction with "+id+" not Found"));
    }

    public List<Transaction> getAllTransaction()
    {
        return transactionRepository.findAll();
    }

    public Transaction updateTransaction(Transaction newTransaction, long id)
    {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()-> new NotFoundException("Transaction with "+id+" not Found"));

        if(transaction == null)
            throw new NotFoundException("Transaction with "+id+" not Found");

        if(newTransaction.getSenderAccount() != 0)
            transaction.setSenderAccount(newTransaction.getSenderAccount());
        if(newTransaction.getSenderCardId() != 0)
            transaction.setSenderCardId(newTransaction.getSenderCardId());
        if(newTransaction.getReceiverAccount() != 0)
                transaction.setReceiverAccount(newTransaction.getReceiverAccount());
        if(newTransaction.getAmount() != 0)
            transaction.setAmount(newTransaction.getAmount());
        return transactionRepository.save(transaction);
    }


    public String deleTransactionById(long id)
    {
        if(transactionRepository.existsById(id))
        {
            transactionRepository.deleteById(id);
            return "DELETED TRANSACTION WITH ID : "+id;
        }
        else
            new NotFoundException("Transaction with "+id+" not Found");
            return "NOT FOUND EXCEPTION";
    }

    public Transaction createTransferTransaction(Transaction transaction)
    {
        if(transaction == null || !validateTransaction(transaction, "transfer")){
            throw new InvalidDataException("Invalid Transaction");
        }
        Transaction _transaction = Transaction
        .builder()
        .senderAccount(transaction.getSenderAccount())
        .senderCardId(transaction.getSenderCardId())
        .receiverAccount(transaction.getReceiverAccount())
        .amount(transaction.getAmount())
        .typeId(transaction.getTypeId())
        .status("SUCCESS")
        .build();
        if(_transaction == null)
            throw new NotFoundException("Transaction Not Found");
        return transactionRepository.save(_transaction);
    }


    public Transaction createWithdrawTransaction(Transaction transaction)
    {
        if(transaction == null || !validateTransaction(transaction, "withdraw")){
            throw new InvalidDataException("Invalid Transaction");
        }

        Random random = new Random();
        Transaction _transaction = Transaction
        .builder()
        .senderAccount(0)
        .receiverAccount(transaction.getReceiverAccount())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .typeId(2)
        .status("SUCCESS")
        .build();

        if(_transaction == null)
            throw new NotFoundException("Transaction Not Found");

        return transactionRepository.save(_transaction);
    }

    public Transaction createDepositTransaction(Transaction transaction)
    {
        if(transaction == null || !validateTransaction(transaction, "deposit")){
            throw new InvalidDataException("Invalid Transaction");
        }

        Random random = new Random();
        Transaction _transaction = Transaction
        .builder()
        .senderAccount(transaction.getSenderAccount())
        .receiverAccount(0)
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .typeId(transaction.getTypeId())
        .status("SUCCESS")
        .build();
        
        if(_transaction == null)
            throw new NotFoundException("Transaction Not Found");

        return transactionRepository.save(_transaction);
    }

    public Transaction createCardTransaction(Transaction transaction)
    {
        if(transaction == null || !validateTransaction(transaction, "card")){
            throw new InvalidDataException("Invalid Transaction");
        }

        
        Transaction _transaction = Transaction
        .builder()
        .senderCardId(transaction.getSenderCardId())
        .receiverAccount(transaction.getReceiverAccount())
        .amount(transaction.getAmount())
        .typeId(transaction.getTypeId())
        .status("SUCCESS")
        .build();
        
        if(_transaction == null)
            throw new NotFoundException("Transaction Not Found");

        return transactionRepository.save(_transaction);
    }


    public boolean validateTransaction(Transaction transaction, String transactionType)
    {
        if(transaction.getAmount() <= 0)
            return false;
        if(!transactionType.equals("card") && transaction.getSenderAccount() == 0)
            return false;
        if(transactionType.equals("transfer") && transaction.getTypeId() == 0)
            return false;
        if(transactionType.equals("card") && transaction.getSenderCardId() == 0)
            return false;
        if((transactionType.equals("transfer") || transactionType.equals("card")) && transaction.getReceiverAccount() == 0)
            return false;

        return true;


    }
}
