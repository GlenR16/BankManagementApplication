package com.wissen.bank.accountservice.services;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.repositories.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    private long makeAccountNumeber(){
        Random rand = new Random();
        long accountNo = rand.nextLong(100000000,999999999);
        return accountNo;
    }


    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account getAccountByAccountNumber(long accountNumber){
    return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Transactional
    public Account createAccount(Account account){
        if (account == null || !validateAccount(account)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account details incomplete.");
        }
        Account _account = Account
        .builder()
        .customerId(account.getCustomerId())                                  
        .accountNumber(makeAccountNumeber())
        .branchId(account.getBranchId())        
        .typeId(account.getTypeId())
        .balance(account.getBalance())
        .withdrawalLimit(account.getWithdrawalLimit())
        .isVerified(false)
        .isActive(true)
        .isLocked(false)
        .isDeleted(false)
        .build();
        return accountRepository.save(_account);
    }

    @Transactional
    public Account updateAccountByAccountNumber(Account newAccount, long accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (newAccount.getBranchId() > 0){
            account.setBranchId(newAccount.getBranchId());
        }
        if (newAccount.getTypeId() > 0){
            account.setTypeId(newAccount.getTypeId());
        }
        if (newAccount.getBalance() > 0){
            account.setBalance(newAccount.getBalance());
        }
        if (newAccount.getWithdrawalLimit() > 0){
            account.setWithdrawalLimit(newAccount.getWithdrawalLimit());
        }
        return accountRepository.save(account);
    }

    @Transactional
    public Account verifyAccountByAccountNumber(long accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.setVerified(true);
        return accountRepository.save(account);
    }

    @Transactional
    public Account switchAccountLockByAccountNumber(long accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (account.isLocked() && account.getUpdatedAt().before(DateUtils.addDays(new Date(), -2))){
            account.setLocked(false);
        }
        else if (!account.isLocked()){
            account.setLocked(true);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be unlocked before 2 days have passed.");
        }
        return accountRepository.save(account);
    }

    private boolean validateAccount(Account account){
        if (account.getCustomerId().isBlank() || account.getBranchId() == 0 || account.getBalance() == 0 || account.getWithdrawalLimit() == 0 || account.getTypeId() == 0){
            return false;
        }
        return true;
    }

    @Transactional
    public Account deleteAccountByAccountNumber(long accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.setDeleted(true);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByCustomerId(String customerId){
        return accountRepository.findByCustomerId(customerId);
    }
}