package com.wissen.bank.transactionservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.repositories.TransactionRepository;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/transaction")

public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public String welcome()
    {
        LOGGER.info("Just Home for Transaction");
        return "Welcome to Transaction Service";
    }
    
        @GetMapping("/view")
        public List<Transaction> getAll()
        {
            return transactionRepository.findAll();
        }

    @PostMapping("/transfer")
    public Transaction tranferAmt(@RequestBody Transaction transaction)
    {
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

        LOGGER.info("Created transaction with ID : ",_transaction.getId());
        transactionRepository.save(_transaction);
        return _transaction;
    }
    

    @PostMapping("/withdraw")
    public Transaction doWithdrawTransaction(@RequestBody Transaction transaction)
    {
        Random random = new Random();
        
        Transaction _transaction = Transaction
        .builder()
        .senderAcc(transaction.getSenderAcc())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .type("WITHDRAW")
        .status(true)
        .build();

        LOGGER.info("Successfully withdraw amount : ",_transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }

    @PostMapping("/deposit")
    public Transaction doDepositTransaction(@RequestBody Transaction transaction)
    {
        Random random = new Random();
        
        Transaction _transaction = Transaction
        .builder()
        .senderAcc(transaction.getSenderAcc())
        .senderCardId(random.nextLong(100000000000L, 999999999999L))
        .amount(transaction.getAmount())
        .type("DEPOSIT")
        .status(true)
        .build();

        LOGGER.info("Successfully Deposit amount : ",_transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }

    @PostMapping("/cardTransfer")
    public Transaction cardTransfer(@RequestBody Transaction transaction)
    {        
        Transaction _transaction = Transaction
        .builder()
        .senderCardId(transaction.getSenderCardId())
        .receiverAcc(transaction.getReceiverAcc())
        .amount(transaction.getAmount())
        // Also Taking PIN for CARD
        .type("CARD")
        .status(true)
        .build();

        LOGGER.info("Card Transaction Success : ",_transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }
}
