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
import com.wissen.bank.accountservice.models.Beneficiary;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.BeneficiaryRepository;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/account/beneficiary")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryRepository beneficiaryRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Beneficiary> getAllBeneficiaries(@RequestHeader("Customer") String customerId,
            @RequestHeader("Role") Role role) {

        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} Displaying all Beneficiaries", customerId);
            return beneficiaryRepo.findAll();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public Beneficiary postBeneficiary(@RequestBody Beneficiary br, @RequestHeader("Customer") String customerId,
            @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Beneficiary ben = Beneficiary
                    .builder()
                    .id(br.getId())
                    .name(br.getName())
                    .accountId(br.getAccountId())
                    .recieverId(br.getRecieverId())
                    .ifsc(br.getIfsc())
                    .build();
            if (ben == null) {
                throw new NotFoundException("Object Null");
            }
            LOGGER.info("Admin {} Created new Beneficiary", customerId);
            return beneficiaryRepo.save(ben);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(@PathVariable long id, @RequestBody Beneficiary br,
            @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            if (beneficiaryRepo.existsById(id)) {
                Beneficiary _ben = beneficiaryRepo.findById(id).orElseThrow();
                _ben.setName(br.getName());
                _ben.setAccountId(br.getAccountId());
                _ben.setRecieverId(br.getRecieverId());
                _ben.setIfsc(br.getIfsc());
                return beneficiaryRepo.save(_ben);

            } else {
                LOGGER.info("Admin {} No Beneficiary with given ID Found", customerId);
            }
            return null;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteBeneficiary(@PathVariable long id, @RequestHeader("Customer") String customerId,
            @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            if (beneficiaryRepo.existsById(id)) {
                LOGGER.info("Admin {} Deleted Beneficiary", customerId);
                beneficiaryRepo.deleteById(id);
                return "Beneficiary Deleted Successfully";
            } else {
                LOGGER.info("No Beneficiary Found with Given ID");
                return "Beneficiary not Found";
            }
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
        Beneficiary ben1 = Beneficiary.builder().id(1).name("Vir").accountId(999999).recieverId(888888).ifsc("1A2B3C")
                .build();
        if (ben1 != null) {
            beneficiaryRepo.save(ben1);
        }
        Beneficiary ben2 = Beneficiary.builder().id(2).name("Jane").accountId(111111).recieverId(222222).ifsc("9A9B9C")
                .build();
        if (ben2 != null) {
            beneficiaryRepo.save(ben2);
        }
    }

}
