package com.wissen.bank.accountservice.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.services.AccountService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("")
    public List<Account> getAllAccounts(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            return accountService.getAllAccounts();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/list")
    public List<Account> getAccounts(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        return accountService.getAccountsByCustomerId(customerId);
    }

    @GetMapping("/{accountNumber}")
    public Account getAccountByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customer)) {
            return  accountService.getAccountByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @PostMapping("")
    public Account createAccount(@RequestBody Account account,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        account.setCustomerId(customerId);
        Account _account = accountService.createAccount(account);
        return _account;
    }

    @PostMapping("/verify/{accountNumber}")
    public Account postVerifyAccount(@PathVariable long accountNumber, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            return accountService.verifyAccountByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PostMapping("/lock/{accountNumber}")
    public Account postLockAccount(@PathVariable long accountNumber, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accountService.getAccountByAccountNumber(accountNumber).getCustomerId().equals(customer)) {
            return accountService.switchAccountLockByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }
    

    @PutMapping("/{accountNumber}")
    public Account updateAccount(@PathVariable long accountNumber, @RequestBody Account account, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Account _account = accountService.getAccountByAccountNumber(accountNumber);
        if ( role == Role.ADMIN || role == Role.EMPLOYEE || _account.getCustomerId().equals(customer)) {
            return accountService.updateAccountByAccountNumber(account, accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @DeleteMapping("/{accountNumber}")
    public Account deleteAccount(@PathVariable long accountNumber, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            return accountService.deleteAccountByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this record.");
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
                .customerId("1111111111")
                .branchId(1)
                .typeId(1)
                .balance(200000)
                .withdrawalLimit(20000)
                .build();
        accountService.createAccount(acc3);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class, SQLIntegrityConstraintViolationException.class })
    public void handleSQLException(Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some fields are already used.");
    }

}
