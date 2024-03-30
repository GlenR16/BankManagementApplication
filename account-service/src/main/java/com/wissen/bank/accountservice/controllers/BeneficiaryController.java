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

import com.wissen.bank.accountservice.exceptions.exceptions.NotFoundException;
import com.wissen.bank.accountservice.models.Beneficiary;
import com.wissen.bank.accountservice.repositories.BeneficiaryRepository;

@RestController
@RequestMapping("/account/beneficiary")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryRepository beneficiaryRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Beneficiary> getAllBeneficiaries(){
        LOGGER.info("Displaying all Beneficiaries");
        return beneficiaryRepo.findAll();
    }

    @PostMapping("")
    public Beneficiary postBeneficiary(@RequestBody Beneficiary br) {
        
        Beneficiary ben = Beneficiary
        .builder()
        .id(br.getId())
        .name(br.getName())
        .account_id(br.getAccount_id())
        .reciever_id(br.getReciever_id())
        .ifsc_code(br.getIfsc_code())
        .build();

        if (ben == null){
            throw new NotFoundException("Object Null");
        }

        LOGGER.info("Created new Beneficiary");
        
        return beneficiaryRepo.save(ben);
    }


    @PutMapping("/{id}")    
    public Beneficiary updateBeneficiary(@PathVariable long id, @RequestBody Beneficiary br) {
        
        if(beneficiaryRepo.existsById(id)){

            Beneficiary _ben = beneficiaryRepo.findById(id).orElseThrow();
            
            _ben.setName(br.getName());
            _ben.setAccount_id(br.getAccount_id());
            _ben.setReciever_id(br.getReciever_id());
            _ben.setIfsc_code(br.getIfsc_code());

            return beneficiaryRepo.save(_ben);

        }
        else{
            LOGGER.info("No Beneficiary with given ID Found");
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteBeneficiary(@PathVariable long id){

        if(beneficiaryRepo.existsById(id)){
            LOGGER.info("Deleted Beneficiary");
            beneficiaryRepo.deleteById(id);
            return "Beneficiary Deleted Successfully";
        }
        else{
            LOGGER.info("No Beneficiary Found with Given ID");
            return "Beneficiary not Found";
        }
    }
    
}
