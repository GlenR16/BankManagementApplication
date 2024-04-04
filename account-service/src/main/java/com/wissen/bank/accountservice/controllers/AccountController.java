package com.wissen.bank.accountservice.controllers;

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
import org.springframework.web.bind.annotation.RestController;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.accountservice.exceptions.UnauthorizedException;
import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.services.AccountService;

import jakarta.annotation.PostConstruct;

import com.wissen.bank.accountservice.responses.Response;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Account> getAllAccounts(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} Getting all accounts", customerId);
            return accountService.getAllAccounts();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/list")
    public List<Account> getAccounts(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        return accountService.getAccountsByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable long id, @RequestHeader("Customer") String customer,
            @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("User " + customer + " Getting Account id: {}", id);
            return accountService.getAccountById(id);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public ResponseEntity<Response> createAccount(@RequestBody Account account, @RequestHeader("Role") Role role) {
        Account _account = accountService.createAccount(account);
        LOGGER.info("Creating account id: {}", _account.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Account created successfully", "/account/"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateAccount(@PathVariable long id, @RequestBody Account account, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Account _account = accountService.getAccountById(id);
        if (_account == null) {
            return ResponseEntity.badRequest().body(new Response(new Date(), 404, "Account not found", "/account/" + id));
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || _account.getCustomerId().equals(customer)) {
            LOGGER.info("Updating Account id: {}", id);
            accountService.updateAccount(account, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Account updated successfully", "/account/" + id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> isDeletedAccount(@PathVariable long id, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        LOGGER.info("Deleting Account id: {}", id);
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok().body(new Response(new Date(), 200,"Deleted account successfully", "/account/" + id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
        Account acc1 = Account.builder()
                .customerId("9999999999")
                .branchId(1)
                .typeId(1)
                .balance(100000)
                .withdrawalLimit(10000)
                .build();
        accountService.createAccount(acc1);
        Account acc2 = Account.builder()
                .customerId("9999999999")
                .branchId(2)
                .typeId(2)
                .balance(200000)
                .withdrawalLimit(20000)
                .build();
        accountService.createAccount(acc2);
        Account acc3 = Account.builder()
                .customerId("8888888888")
                .branchId(2)
                .typeId(2)
                .balance(200000)
                .withdrawalLimit(20000)
                .build();
        accountService.createAccount(acc3);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class })
    public ResponseEntity<Response> handleSQLException(Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }

}
