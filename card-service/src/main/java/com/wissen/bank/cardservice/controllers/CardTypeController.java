package com.wissen.bank.cardservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.CardType;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.repositories.CardTypeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/card/cardType")
public class CardTypeController {

    @Autowired
    private CardTypeRepository cardTypeRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<CardType> getAllCardTypes(@RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Admin {} Displaying all CardTypes",customer);
            return cardTypeRepo.findAll();
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public CardType postCardType(@RequestBody CardType ct, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            CardType _cardType = CardType
            .builder()
            .id(ct.getId())
            .name(ct.getName())
            .interest(ct.getInterest())
            .build();

            if (_cardType == null){
                throw new NotFoundException("card Type Object Null");
            }
        
            LOGGER.info("Admin {} Created new Card Type",customer);
            
            return cardTypeRepo.save(_cardType);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public CardType updateCardType(@PathVariable long id, @RequestBody CardType br, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if(cardTypeRepo.existsById(id)){

                CardType _cdt = cardTypeRepo.findById(id).orElseThrow();
                
                _cdt.setName(br.getName());
                _cdt.setInterest(br.getInterest());

                LOGGER.info("Admin {} Updating card details id : ",id, "Customer : ",customer);

                return cardTypeRepo.save(_cdt);

            }
            else{
                LOGGER.info("No CardType with given ID Found",customer);
            }
            return null;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    
    @DeleteMapping("/{id}")
    public String deleteCardType(@PathVariable long id, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {

            if(cardTypeRepo.existsById(id)){
                LOGGER.info("Admin {} Deleted CardType",customer);
                cardTypeRepo.deleteById(id);
                return "CardType Deleted Successfully";
            }
            else{
                LOGGER.info("Admin {} No CardType Found with Given ID",customer);
                return "CardType not Found";
            }
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init(){
        CardType cardType1 = CardType.builder()
                .name("Debit")
                .interest(0.0)
                .build();
        if (cardType1 != null) {
            cardTypeRepo.save(cardType1);
        }

        CardType cardType2 = CardType.builder()
                .name("Credit")
                .interest(0.1)
                .build();
        if (cardType2 != null) {
            cardTypeRepo.save(cardType2);
        }

    }

    
}
