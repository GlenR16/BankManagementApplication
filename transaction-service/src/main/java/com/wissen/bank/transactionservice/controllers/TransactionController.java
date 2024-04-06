package com.wissen.bank.transactionservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.transactionservice.exceptions.UnauthorizedException;
import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.responses.Response;
import com.wissen.bank.transactionservice.services.TransactionService;

import jakarta.annotation.PostConstruct;

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
    public List<Transaction> getAllTransactions(@RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("ADmin {} Getting all Transactions",customerId);
            return transactionservice.getAllTransaction();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER) {
            LOGGER.info("Admin {} Getting transaction id : ", id, "Customer : ",customerId);
            return transactionservice.getTransactionByTransactionId(id);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response> createTransfer(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER) 
        {
            Transaction _transaction = transactionservice.createTransferTransaction(transaction);
            LOGGER.info("Creating Transfer Transaction with id : ", _transaction.getId(),"Cusotmer : ",customerId);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Transfer Success", _transaction));
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response> createWithdraw(@RequestBody Transaction transaction,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        
        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER) 
        {
            Transaction _transaction = transactionservice.createWithdrawTransaction(transaction);
            LOGGER.info("Creating Withdraw Transaction with id : ", _transaction.getId(), "Customr : ",customerId);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Withdraw Success", _transaction));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/deposit")
    public ResponseEntity<Response> createDeposit(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER) 
        {
            Transaction _transaction = transactionservice.createDepositTransaction(transaction);
            LOGGER.info("Creating Deposit Transaction with id : ", _transaction.getId(), "Customer : ",customerId);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deposit Success", _transaction));
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/cardTransfer")
    public ResponseEntity<Response> createCardTransfer(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER) 
        {
            Transaction _transaction = transactionservice.createCardTransaction(transaction);
            LOGGER.info("Creating Card Transaction with id : ", _transaction.getId(),"Customer Id : ",customerId);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "CardTransfer Success", _transaction));
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTransaction(@PathVariable long id, @RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Updating Transaction ID : ", transaction.getId(),"Cusotmer Id : ",customerId);

            transactionservice.updateTransaction(transaction, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Transaction Updated success", transaction));
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTransaction(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role)
    {
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Deleting Transaction with id : ",id, "Cusotmer : ",customerId);
            transactionservice.deleTransactionById(id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deleted Transaction Success with id : "+id, new Transaction()));
        }

        throw new UnauthorizedException("Unauthorized");
            
    }


    @PostConstruct
    public void init(){
        Transaction txn = Transaction.builder()
            .senderAccount(99999)
            .senderCardId(452343)
            .receiverAccount(888888)
            .amount(300000)
            .typeId(2)
            .build();
        transactionservice.createTransferTransaction(txn);

    }


    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Response> handleSQLException(Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }
}
 