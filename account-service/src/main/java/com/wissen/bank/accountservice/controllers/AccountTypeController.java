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

import com.wissen.bank.accountservice.exceptions.exceptions.NotFoundException;
import com.wissen.bank.accountservice.exceptions.exceptions.UnauthorizedException;
import com.wissen.bank.accountservice.models.AccountType;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.AccountTypeRepository;

@RestController
@RequestMapping("/account/accounttype")
public class AccountTypeController {

    @Autowired
    private AccountTypeRepository accTypeRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<AccountType> getAllAccountTypes(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){

        if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {        
            LOGGER.info("Admin {} Displaying all Account Types",customerId);
            return accTypeRepo.findAll();
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public AccountType postAccountType(@RequestBody AccountType br, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
         if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            AccountType _accType = AccountType
            .builder()
            .id(br.getId())
            .name(br.getName())
            .build();

            if (_accType == null){
                throw new NotFoundException("Account Null");
            }

            LOGGER.info("Admin {} Created new AccountType",customerId);
            
            return accTypeRepo.save(_accType);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public AccountType updateAccountType(@PathVariable long id, @RequestBody AccountType br, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
         if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
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

        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteAccountType(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
         if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(accTypeRepo.existsById(id)){
                LOGGER.info("Admin {} Deleted Account Type",customerId);
                accTypeRepo.deleteById(id);
                return "Account Type Deleted Successfully";
            }
            else{
                LOGGER.info("No Account Type Found with Given ID");
                return "Account Type not Found";
            }
        }

        throw new UnauthorizedException("Unauthorized");
    }


    
}
