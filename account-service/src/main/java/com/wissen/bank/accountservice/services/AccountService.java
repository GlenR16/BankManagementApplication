package com.wissen.bank.accountservice.services;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.accountservice.exceptions.exceptions.NotFoundException;
import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.repositories.AccountRepository;

@Service
public class AccountService {

    public long makeAccountNumeber(){
        Random rand = new Random();
        long accountNo = rand.nextLong(100000000,999999999);
        return accountNo;
    }

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id){
        if (id == null){
            throw new IllegalArgumentException("Invalid account");
        }
        return accountRepository.findById(id).orElseThrow(()-> new NotFoundException("Account Not Found"));
    }

    public Account createAccount(Account account){
        if (account == null || !validateAccount(account)){
            throw new IllegalArgumentException("Invalid account");
        }
        Account _account = Account
        .builder()
        .user_id(account.getUser_id())                                   
        .account_number(makeAccountNumeber())
        .branch_id(account.getBranch_id())        
        .type_id(account.getType_id())
        .balance(account.getBalance())
        .withdrawal_limit(account.getWithdrawal_limit())
        .is_verified(true)
        .is_active(true)
        .is_locked(false)
        .is_deleted(false)
        .build();
        if (_account == null){
            throw new NotFoundException("account Not Found");
        }
        return accountRepository.save(_account);
    }

    public Account updateAccount(Account newAccount, Long id){

        if (id == null){
            throw new IllegalArgumentException("Invalid account");
        }

        Account account = accountRepository.findById(id).orElseThrow(()-> new NotFoundException("Account Not Found"));
        if (account == null){
            throw new NotFoundException("Account Not Found");
        }
        if (newAccount.getUser_id() > 0){
            account.setUser_id(newAccount.getUser_id());
        }
        if (newAccount.getBranch_id() > 0){
            account.setBranch_id(newAccount.getBranch_id());
        }
        if (newAccount.getType_id() > 0){
            account.setType_id(newAccount.getType_id());
        }
        if (newAccount.getBalance() > 0){
            account.setBalance(newAccount.getBalance());
        }
        if (newAccount.getWithdrawal_limit() > 0){
            account.setWithdrawal_limit(newAccount.getWithdrawal_limit());
        }
        
        return accountRepository.save(account);
    }


    public boolean validateAccount(Account account){
        if (account.getUser_id() == 0|| account.getBranch_id() == 0 || account.getBalance() == 0 || account.getWithdrawal_limit() == 0 || account.getType_id() == 0){
            return false;
        }
        return true;
    }

    public Account deleteAccountById(long id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new NotFoundException("account Not Found"));
        account.set_deleted(true);
        return accountRepository.save(account);
    }

}