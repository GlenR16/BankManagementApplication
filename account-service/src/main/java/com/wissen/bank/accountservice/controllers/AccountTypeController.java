package com.wissen.bank.accountservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.exceptions.NotFoundException;
import com.wissen.bank.accountservice.exceptions.UnauthorizedException;
import com.wissen.bank.accountservice.models.AccountType;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.AccountTypeRepository;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/account/type")
public class AccountTypeController {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
            if (_accountType == null) {
                throw new NotFoundException("Account not found");
            }
            LOGGER.info("Admin {} Created new AccountType", customerId);
            return accountTypeRepository.save(_accountType);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public AccountType updateAccountType(@PathVariable long id, @RequestBody AccountType accountType,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            AccountType _accountType = accountTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Account type not found"));
            if (!accountType.getName().isBlank()) _accountType.setName(accountType.getName());
            accountTypeRepository.save(_accountType);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteAccountType(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            AccountType accountType = accountTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Account type not found"));
            accountTypeRepository.delete(accountType);
            LOGGER.info("Admin {} deleted account type {}", customerId , id);
        }
        throw new UnauthorizedException("Unauthorized");
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
