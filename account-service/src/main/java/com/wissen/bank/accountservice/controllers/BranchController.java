package com.wissen.bank.accountservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.accountservice.models.Branch;
import com.wissen.bank.accountservice.repositories.BranchRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/account/branch")
public class BranchController {

    @Autowired
    private BranchRepository branchRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Branch> getAllBranches(){
        LOGGER.info("Displaying all Branches");
        return branchRepo.findAll();
    }

    @PostMapping("")
    public Branch postBranch(@RequestBody Branch br) {
        
        Branch _branch = Branch
        .builder()
        .id(br.getId())
        .name(br.getName())
        .address(br.getAddress())
        .ifsc_code(br.getIfsc_code())
        .build();

        LOGGER.info("Created new Branch");
        
        return branchRepo.save(_branch);
    }

    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable long id, @RequestBody Branch br) {
        
        if(branchRepo.existsById(id)){

            Branch _branch = branchRepo.findById(id).orElseThrow();
            
            _branch.setName(br.getName());
            _branch.setAddress(br.getAddress());
            _branch.setIfsc_code(br.getIfsc_code());

            return branchRepo.save(_branch);

        }
        else{
            LOGGER.info("No Branch with given ID Found");
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteBranch(@PathVariable long id){

        if(branchRepo.existsById(id)){
            LOGGER.info("Deleted Branch");
            branchRepo.deleteById(id);
            return "Branch Deleted Successfully";
        }
        else{
            LOGGER.info("No Branch Found with Given ID");
            return "Branch not Found";
        }
    }
    
}
