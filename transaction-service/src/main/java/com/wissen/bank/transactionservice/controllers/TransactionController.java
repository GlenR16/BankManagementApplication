package com.wissen.bank.transactionservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.transactionservice.exceptions.UnauthorizedException;
import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.responses.Response;
import com.wissen.bank.transactionservice.services.TransactionService;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/transaction")

public class TransactionController {

    @Autowired
    private TransactionService transactionservice;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Transaction> getAllTransactions(@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("ADmin {} Getting all Transactions");
            return transactionservice.getAllTransaction();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} Getting transaction id : ", id);
            return transactionservice.getTransactionByTransactionId(id);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response> createTransfer(@RequestBody Transaction transaction) {
        Transaction _transaction = transactionservice.createTransferTransaction(transaction);
        LOGGER.info("Creating Transfer Transaction with id : ", _transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Transfer Success", transaction));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response> createWithdraw(@RequestBody Transaction transaction) {
        Transaction _transaction = transactionservice.createWithdrawTransaction(transaction);
        LOGGER.info("Creating Withdraw Transaction with id : ", _transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Withdraw Success", transaction));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Response> createDeposit(@RequestBody Transaction transaction) {
        Transaction _transaction = transactionservice.createDepositTransaction(transaction);
        LOGGER.info("Creating Deposit Transaction with id : ", _transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Deposit Success", transaction));
    }

    @PostMapping("/cardTransfer")
    public ResponseEntity<Response> createCardTransfer(@RequestBody Transaction transaction) {
        Transaction _transaction = transactionservice.createCardTransaction(transaction);
        LOGGER.info("Creating Card Transaction with id : ", _transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "CardTransfer Success", transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTransaction(@PathVariable long id, @RequestBody Transaction transaction,
            @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Updating Transaction ID : ", transaction.getId());

            transactionservice.updateTransaction(transaction, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Transaction Updated success", transaction));
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTransaction(@PathVariable long id, @RequestHeader("Role") Role role)
    {
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Deleting Transaction with id : ",id);
            transactionservice.deleTransactionById(id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deleted Transaction Success with id : "+id, new Transaction()));
        }

        throw new UnauthorizedException("Unauthorized");
            
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class, IllegalArgumentException.class})
    public ResponseEntity<Response> handleSQLException(Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }
    


    /*
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
    public Transaction doWithdrawTransaction(@RequestBody Transaction transaction) {
        Random random = new Random();

        Transaction _transaction = Transaction
                .builder()
                .senderAcc(transaction.getSenderAcc())
                .senderCardId(random.nextLong(100000000000L, 999999999999L))
                .amount(transaction.getAmount())
                .type("WITHDRAW")
                .status(true)
                .build();

        LOGGER.info("Successfully withdraw amount : ", _transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }

    @PostMapping("/deposit")
    public Transaction doDepositTransaction(@RequestBody Transaction transaction) {
        Random random = new Random();

        Transaction _transaction = Transaction
                .builder()
                .senderAcc(transaction.getSenderAcc())
                .senderCardId(random.nextLong(100000000000L, 999999999999L))
                .amount(transaction.getAmount())
                .type("DEPOSIT")
                .status(true)
                .build();

        LOGGER.info("Successfully Deposit amount : ", _transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }

    @PostMapping("/cardTransfer")
    public Transaction cardTransfer(@RequestBody Transaction transaction) {
        Transaction _transaction = Transaction
                .builder()
                .senderCardId(transaction.getSenderCardId())
                .receiverAcc(transaction.getReceiverAcc())
                .amount(transaction.getAmount())
                // Also Taking PIN for CARD
                .type("CARD")
                .status(true)
                .build();

        LOGGER.info("Card Transaction Success : ", _transaction.getAmount());
        transactionRepository.save(_transaction);
        return _transaction;
    }
    */
}
 