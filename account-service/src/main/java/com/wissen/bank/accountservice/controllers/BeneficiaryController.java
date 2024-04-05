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
            beneficiaries.addAll(beneficiaryRepository.findByAccountId(account.getId()));
        });
        return beneficiaries;
    }
    

    @PostMapping("")
    public Beneficiary postBeneficiary(@RequestBody Beneficiary benificiary, @RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        Beneficiary _benificiary = Beneficiary
                .builder()
                .id(benificiary.getId())
                .name(benificiary.getName())
                .accountId(benificiary.getAccountId())
                .recieverId(benificiary.getRecieverId())
                .ifsc(benificiary.getIfsc())
                .build();
        if (_benificiary == null) {
            throw new NotFoundException("Object Null");
        }
        LOGGER.info("User {} created new beneficiary", customerId);
        return beneficiaryRepository.save(_benificiary);
    }

    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(@PathVariable long id, @RequestBody Beneficiary beneficiary, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Beneficiary _beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Beneficiary not found"));
            if (!beneficiary.getName().isBlank()) _beneficiary.setName(beneficiary.getName());
            if (beneficiary.getAccountId() > 0) _beneficiary.setAccountId(beneficiary.getAccountId());
            if (beneficiary.getRecieverId() > 0) _beneficiary.setRecieverId(beneficiary.getRecieverId());
            if (!beneficiary.getIfsc().isBlank()) _beneficiary.setIfsc(beneficiary.getIfsc());
            return beneficiaryRepository.save(_beneficiary);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBeneficiary(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Beneficiary beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new NotFoundException("Beneficiary not found"));
            beneficiaryRepository.delete(beneficiary);
            LOGGER.info("Admin {} deleted Beneficiary {}", customerId, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deleted Beneficiary successfully", "/account/beneficiary/" + id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
        Beneficiary ben1 = Beneficiary.builder().id(1).name("Vir").accountId(999999).recieverId(888888).ifsc("1A2B3C")
                .build();
        if (ben1 != null) {
            beneficiaryRepository.save(ben1);
        }
        Beneficiary ben2 = Beneficiary.builder().id(2).name("Jane").accountId(111111).recieverId(222222).ifsc("9A9B9C")
                .build();
        if (ben2 != null) {
            beneficiaryRepository.save(ben2);
        }
    }

}
