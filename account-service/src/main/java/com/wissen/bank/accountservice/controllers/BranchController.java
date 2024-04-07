package com.wissen.bank.accountservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.exceptions.NotFoundException;
import com.wissen.bank.accountservice.exceptions.UnauthorizedException;
import com.wissen.bank.accountservice.models.Branch;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.BranchRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/account/branch")
public class BranchController {

    @Autowired
    private BranchRepository branchRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Branch> getAllBranches(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        return branchRepo.findAll();
    }

    @PostMapping("")
    public Branch postBranch(@RequestBody Branch branch, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Branch _branch = Branch
                .builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .ifsc(branch.getIfsc())
                .build();
            if (_branch == null) {
                throw new NotFoundException("Branch Object Null");
            }
            LOGGER.info("Admin {} created new branch", customerId);
            return branchRepo.save(_branch);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable long id, @RequestBody Branch branch,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Branch _branch = branchRepo.findById(id).orElseThrow(() -> new NotFoundException("Branch not found"));
            if (!branch.getAddress().isBlank()) _branch.setAddress(branch.getAddress());
            if (!branch.getIfsc().isBlank()) _branch.setIfsc(branch.getIfsc());
            if (!branch.getName().isBlank()) _branch.setName(branch.getName());
            return branchRepo.save(_branch);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteBranch(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            Branch branch = branchRepo.findById(id).orElseThrow(() -> new NotFoundException("Branch not found"));
            branchRepo.delete(branch);
            LOGGER.info("Branch {} deleted by admin {}",id,customerId);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
        Branch branch1 = Branch.builder()
                .name("Colaba")
                .address("Mumbai")
                .ifsc("1A2B3C")
                .build();
        if (branch1 != null) {
            branchRepo.save(branch1);
        }

        Branch branch2 = Branch.builder()
                .name("Kalyan")
                .address("Thane")
                .ifsc("9A9B9C")
                .build();
        if (branch2 != null) {
            branchRepo.save(branch2);
        }
    }

}
