package com.wissen.bank.accountservice.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.wissen.bank.accountservice.models.Account;
import com.wissen.bank.accountservice.models.Beneficiary;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.BeneficiaryRepository;
import com.wissen.bank.accountservice.responses.Response;
import com.wissen.bank.accountservice.services.AccountService;

import jakarta.annotation.PostConstruct;




@RestController
@RequestMapping("/account/beneficiary")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountService accountService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Beneficiary> getAllBeneficiaries(@RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} displaying all beneficiaries", customerId);
            return beneficiaryRepository.findAll();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/list")
    public List<Beneficiary> getBeneficiaries(@RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role){
        List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
        List<Beneficiary> beneficiaries = new ArrayList<>();
        accounts.stream().forEach(account -> {
            LOGGER.info("User {} getting all beneficiaries", customerId);
            beneficiaries.addAll(beneficiaryRepository.findByAccountNumber(account.getAccountNumber()));
        });
        return beneficiaries;
    }

    @GetMapping("/{accountNumber}")
    public List<Beneficiary> getBeneficiariesByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId)) {
            LOGGER.info("User {} getting all beneficiaries by account number: {}", customerId, accountNumber);
            return beneficiaryRepository.findByAccountNumber(accountNumber);
        }
        throw new UnauthorizedException("Unauthorized");
    }
    
    

    @PostMapping("")
    public Beneficiary postBeneficiary(@RequestBody Beneficiary benificiary, @RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        Account account = accountService.getAccountByAccountNumber(benificiary.getAccountNumber());
        if (account.getCustomerId().equals(customerId)) {
            Beneficiary _benificiary = Beneficiary
                    .builder()
                    .id(benificiary.getId())
                    .name(benificiary.getName())
                    .accountNumber(benificiary.getAccountNumber())
                    .recieverNumber(benificiary.getRecieverNumber())
                    .ifsc(benificiary.getIfsc())
                    .build();
            if (_benificiary == null) {
                throw new NotFoundException("Object Null");
            }
            LOGGER.info("User {} created new beneficiary", customerId);
            return beneficiaryRepository.save(_benificiary);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(@PathVariable long id, @RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Beneficiary not found"));
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accountService.getAccountByAccountNumber(beneficiary.getAccountNumber()).getCustomerId().equals(customerId) ) {
            LOGGER.info("User {} getting beneficiary id : ", id);
            return beneficiary;
        }
        throw new UnauthorizedException("Unauthorized");
    }
    

    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(@PathVariable long id, @RequestBody Beneficiary beneficiary, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Beneficiary _beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Beneficiary not found"));
            if (!beneficiary.getName().isBlank()) _beneficiary.setName(beneficiary.getName());
            if (beneficiary.getAccountNumber() > 0) _beneficiary.setAccountNumber(beneficiary.getAccountNumber());
            if (beneficiary.getRecieverNumber() > 0) _beneficiary.setRecieverNumber(beneficiary.getRecieverNumber());
            if (!beneficiary.getIfsc().isBlank()) _beneficiary.setIfsc(beneficiary.getIfsc());
            return beneficiaryRepository.save(_beneficiary);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBeneficiary(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Beneficiary not found"));
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accountService.getAccountByAccountNumber(beneficiary.getAccountNumber()).getCustomerId().equals(customerId) ) {
            beneficiaryRepository.delete(beneficiary);
            LOGGER.info("Admin {} deleted Beneficiary {}", customerId, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deleted Beneficiary successfully", "/account/beneficiary/" + id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
        Beneficiary ben1 = Beneficiary.builder()
        .accountNumber(1234567890l)
        .recieverNumber(1234567890l)
        .ifsc("IFSC1234")
        .name("Ben1")
        .build();
        beneficiaryRepository.save(ben1);
    }

}
