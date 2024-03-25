package com.wissen.bank.accountservice.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.repositories.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public String home(){
        return "Welcome to Accounts Service";
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts(){
        LOGGER.info("Getting all Accounts");
        return accountRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Account> getAccountById(@PathVariable long id){
        LOGGER.info("Getting Account by id: {}",id);
        return accountRepository.findById(id);
    }

    
    @PostMapping("/register")
    public String createUser(@RequestBody Account account){

        Random rand = new Random();
        long randomNumber = rand.nextInt(1000000000);
        long randomNumber1 = rand.nextInt(1000000000);
        long accountNo = randomNumber;
        long idNo = randomNumber1;

        String ifsc1 = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        Account _account = Account
        .builder()
        .user_id(idNo)                                   
        .account_number(accountNo)
        .ifsc_code(ifsc1)        
        .type_id(account.getType_id())
        .balance(0)
        .withdrawal_limit(account.getWithdrawal_limit())
        .is_verified(true)
        .is_active(true)
        .is_locked(false)
        .is_deleted(false)
        .build();

        LOGGER.info("Creating account id: {}",_account.getId());
        accountRepository.save(_account);
        return "Account Created";
    }


    @PutMapping("/update/{id}")
    public Account updateAccount(@PathVariable long id, @RequestBody Account account){
        Optional<Account> _account = accountRepository.findById(id);
        if (_account.isEmpty()){
            return null;
        }
        Account __account = _account.get();
        __account.setType_id(account.getType_id());
        __account.setBalance(account.getBalance());
        __account.setWithdrawal_limit(account.getWithdrawal_limit());

        LOGGER.info("Updating account id: {}",__account.getId());
        return accountRepository.save(__account);
    }


    @DeleteMapping("/delete/{id}")
    public String deleteAccount(@PathVariable long id){
        LOGGER.info("Deleting account id: {}",id);
        accountRepository.deleteById(id);
        return "Account Deleted";
    }
}
