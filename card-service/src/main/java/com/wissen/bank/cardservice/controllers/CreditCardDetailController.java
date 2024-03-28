package com.wissen.bank.cardservice.controllers;

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

import com.wissen.bank.cardservice.models.CreditCardDetail;
import com.wissen.bank.cardservice.repositories.CreditCardDetailRepository;

@RestController
@RequestMapping("/card/creditCardDetails")
public class CreditCardDetailController {

    @Autowired
    private CreditCardDetailRepository creditCardDetailRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @GetMapping("")
    public List<CreditCardDetail> getAllCreditCardDetails(){
        LOGGER.info("Displaying all CreditCardDetails");
        return creditCardDetailRepo.findAll();
    }

    @PostMapping("")
    public CreditCardDetail postCreditCardDetail(@RequestBody CreditCardDetail ccd) {
        
        CreditCardDetail _CreditCardDetail = CreditCardDetail
        .builder()
        .id(ccd.getId())
        .card_id(ccd.getCard_id())
        .credit_limit(ccd.getCredit_limit())
        .credit_used(ccd.getCredit_used())
        .credit_transactions(ccd.getCredit_transactions())
        .build();

        LOGGER.info("Created new CreditCardDetail");
        
        return creditCardDetailRepo.save(_CreditCardDetail);
    }

    @PutMapping("/{id}")
    public CreditCardDetail updateCardType(@PathVariable long id, @RequestBody CreditCardDetail ccd) {
        
        if(creditCardDetailRepo.existsById(id)){

            CreditCardDetail _ccd = creditCardDetailRepo.findById(id).orElseThrow();
            
            _ccd.setCredit_limit(ccd.getCredit_limit());
            _ccd.setCredit_used(ccd.getCredit_used());

            return creditCardDetailRepo.save(_ccd);

        }
        else{
            LOGGER.info("No CreditCardDetail with given ID Found");
            return null;
        }
    }

    
    @DeleteMapping("/{id}")
    public String deleteCardType(@PathVariable long id){

        if(creditCardDetailRepo.existsById(id)){
            LOGGER.info("Deleted CreditCardDetail");
            creditCardDetailRepo.deleteById(id);
            return "CreditCardDetail Deleted Successfully";
        }
        else{
            LOGGER.info("No CreditCardDetail Found with Given ID");
            return "CreditCardDetail not Found";
        }
    }


}
