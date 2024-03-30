package com.wissen.bank.accountservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.exceptions.exceptions.NotFoundException;
import com.wissen.bank.accountservice.exceptions.exceptions.UnauthorizedException;
import com.wissen.bank.accountservice.models.Branch;
import com.wissen.bank.accountservice.models.Role;
import com.wissen.bank.accountservice.repositories.BranchRepository;
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
    public List<Branch> getAllBranches(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Admin {} Displaying all Branches", customerId);
            return branchRepo.findAll();
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public Branch postBranch(@RequestBody Branch br, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        
        if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            Branch _branch = Branch
            .builder()
            .id(br.getId())
            .name(br.getName())
            .address(br.getAddress())
            .ifsc_code(br.getIfsc_code())
            .build();

            if (_branch == null){
                throw new NotFoundException("Branch Object Null");
            }

            LOGGER.info("Admin {} Created new Branch", customerId);
            
            return branchRepo.save(_branch);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable long id, @RequestBody Branch br, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        
        if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(branchRepo.existsById(id)){

                Branch _branch = branchRepo.findById(id).orElseThrow();
                
                _branch.setName(br.getName());
                _branch.setAddress(br.getAddress());
                _branch.setIfsc_code(br.getIfsc_code());

                LOGGER.info("Admin {} Updating Branch with id : ",id);
                return branchRepo.save(_branch);

            }
            else{
                LOGGER.info("Admin {} No Branch with given ID Found", customerId);
            }
            return null;
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteBranch(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){

        if (role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(branchRepo.existsById(id)){
                LOGGER.info("Admin {} Deleted Branch", customerId);
                branchRepo.deleteById(id);
                return "Branch Deleted Successfully";
            }
            else{
                LOGGER.info("No Branch Found with Given ID");
                return "Branch not Found";
            }
        }

        throw new UnauthorizedException("Unauthorized");
    }
    
}
