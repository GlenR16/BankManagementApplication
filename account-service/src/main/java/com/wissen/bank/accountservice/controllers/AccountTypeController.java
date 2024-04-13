package com.wissen.bank.accountservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.accountservice.models.AccountType;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.AccountTypeRepository;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/account/type")
public class AccountTypeController {

    @Autowired
    private AccountTypeRepository accountTypeRepository;


    @GetMapping("")
    public List<AccountType> getAllAccountTypes() {
        return accountTypeRepository.findAll();
    }

    @PostMapping("")
    public AccountType postAccountType(@RequestBody AccountType accountType,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            AccountType _accountType = AccountType
                .builder()
                .id(accountType.getId())
                .name(accountType.getName())
                .build();
            return accountTypeRepository.save(_accountType);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PutMapping("/{id}")
    public AccountType updateAccountType(@PathVariable long id, @RequestBody AccountType accountType,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            AccountType _accountType = accountTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account type not found"));
            if (!accountType.getName().isBlank()) _accountType.setName(accountType.getName());
            return accountTypeRepository.save(_accountType);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @DeleteMapping("/{id}")
    public AccountType deleteAccountType(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            AccountType accountType = accountTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account type not found"));
            accountTypeRepository.delete(accountType);
            return accountType;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this record.");
    }

    @PostConstruct
    public void init() {
        AccountType accType1 = AccountType.builder()
                .name("Savings")
                .build();
        if (accType1 != null) {
            accountTypeRepository.save(accType1);
        }

        AccountType accType2 = AccountType.builder()
                .name("Current")
                .build();
        if (accType2 != null) {
            accountTypeRepository.save(accType2);
        }
    }

}
