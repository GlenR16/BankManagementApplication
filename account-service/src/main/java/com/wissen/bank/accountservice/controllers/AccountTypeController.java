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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.models.AccountType;
import com.wissen.bank.accountservice.repositories.AccountTypeRepository;

@RestController
@RequestMapping("/account/accounttype")
public class AccountTypeController {

    @Autowired
    private AccountTypeRepository accTypeRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<AccountType> getAllAccountTypes(){
        LOGGER.info("Displaying all Account Types");
        return accTypeRepo.findAll();
    }

    @PostMapping("")
    public AccountType postAccountType(@RequestBody AccountType br) {
        
        AccountType _accType = AccountType
        .builder()
        .id(br.getId())
        .name(br.getName())
        .build();

        LOGGER.info("Created new AccountType");
        
        return accTypeRepo.save(_accType);
    }

    @PutMapping("/{id}")
    public AccountType updateAccountType(@PathVariable long id, @RequestBody AccountType br) {
        
        if(accTypeRepo.existsById(id)){

            AccountType _accType = accTypeRepo.findById(id).orElseThrow();
            
            _accType.setName(br.getName());
            return accTypeRepo.save(_accType);
        }
        else{
            LOGGER.info("No Account Type with given ID Found");
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteAccountType(@PathVariable long id){

        if(accTypeRepo.existsById(id)){
            LOGGER.info("Deleted Account Type");
            accTypeRepo.deleteById(id);
            return "Account Type Deleted Successfully";
        }
        else{
            LOGGER.info("No Account Type Found with Given ID");
            return "Account Type not Found";
        }
    }


    
}
