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

        if(newTransaction.getSenderAcc() != 0)
            transaction.setSenderAcc(newTransaction.getSenderAcc());
        if(newTransaction.getSenderCardId() != 0)
            transaction.setSenderCardId(newTransaction.getSenderCardId());
        if(newTransaction.getReceiverAcc() != 0)
                transaction.setReceiverAcc(newTransaction.getReceiverAcc());
        if(newTransaction.getAmount() != 0)
            transaction.setAmount(newTransaction.getAmount());
        if(!newTransaction.getType().isBlank())
            transaction.setType(newTransaction.getType());
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

        Random random = new Random();
        Transaction _transaction = Transaction
        .builder()
        .senderAcc(transaction.getSenderAcc())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .receiverAcc(transaction.getReceiverAcc())
        .amount(transaction.getAmount())
        .type(transaction.getType())
        .status(true)
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
        .senderAcc(transaction.getSenderAcc())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .type("WITHDRAW")
        .status(true)
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
        .senderAcc(transaction.getSenderAcc())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .type("DEPOSIT")
        .status(true)
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
        .receiverAcc(transaction.getReceiverAcc())
        .amount(transaction.getAmount())
        .type("CARD")
        .status(true)
        .build();
        
        if(_transaction == null)
            throw new NotFoundException("Transaction Not Found");

        return transactionRepository.save(_transaction);
    }


    public boolean validateTransaction(Transaction transaction, String transactionType)
    {
        if(transaction.getAmount() <= 0)
            return false;
        if(!transactionType.equals("card") && transaction.getSenderAcc() == 0)
            return false;
        if(transactionType.equals("transfer") && transaction.getType().isBlank())
            return false;
        if(transactionType.equals("card") && transaction.getSenderCardId() == 0)
            return false;
        if((transactionType.equals("transfer") || transactionType.equals("card")) && transaction.getReceiverAcc() == 0)
            return false;

        return true;


    }
}
