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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.CreditCardDetail;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.repositories.CreditCardDetailRepository;

import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/card/creditCardDetails")
public class CreditCardDetailController {

    @Autowired
    private CreditCardDetailRepository creditCardDetailRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @GetMapping("")
    public List<CreditCardDetail> getAllCreditCardDetails( @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Admin {} Displaying all CreditCardDetails",customer);
            return creditCardDetailRepo.findAll();
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public CreditCardDetail postCreditCardDetail(@RequestBody CreditCardDetail ccd, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            CreditCardDetail _CreditCardDetail = CreditCardDetail
            .builder()
            .id(ccd.getId())
            .card_id(ccd.getCard_id())
            .credit_limit(ccd.getCredit_limit())
            .credit_used(ccd.getCredit_used())
            .credit_transactions(ccd.getCredit_transactions())
            .build();

            if (_CreditCardDetail == null){
                throw new NotFoundException("Credit Card Details Object Null");
            }

            LOGGER.info("Admin {} Created new CreditCardDetail",customer);
            
            return creditCardDetailRepo.save(_CreditCardDetail);
        }
        
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public CreditCardDetail updateCardType(@PathVariable long id, @RequestBody CreditCardDetail ccd, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(creditCardDetailRepo.existsById(id)){

                CreditCardDetail _ccd = creditCardDetailRepo.findById(id).orElseThrow();
                
                _ccd.setCredit_limit(ccd.getCredit_limit());
                _ccd.setCredit_used(ccd.getCredit_used());


                LOGGER.info("Admin {} Updating card details id : ",id, "Customer : ",customer);

                return creditCardDetailRepo.save(_ccd);

            }
            else{
                LOGGER.info("Admin {} No CreditCardDetail with given ID Found",customer);
                return null;
            }
        }
        
        throw new UnauthorizedException("Unauthorized");
    }

    
    @DeleteMapping("/{id}")
    public String deleteCardType(@PathVariable long id, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(creditCardDetailRepo.existsById(id)){
                LOGGER.info("Admin {} Deleted CreditCardDetail",customer);
                creditCardDetailRepo.deleteById(id);
                return "CreditCardDetail Deleted Successfully";
            }
            else{
                LOGGER.info("Admin {} No CreditCardDetail Found with Given ID",customer);
                return "CreditCardDetail not Found";
            }
        }
        
        throw new UnauthorizedException("Unauthorized");
    }


}
