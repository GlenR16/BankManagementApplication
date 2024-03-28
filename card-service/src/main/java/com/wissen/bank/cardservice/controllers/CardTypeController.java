package com.wissen.bank.cardservice.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.cardservice.models.CardType;
import com.wissen.bank.cardservice.repositories.CardTypeRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/card/cardType")
public class CardTypeController {

    @Autowired
    private CardTypeRepository cardTypeRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<CardType> getAllCardTypes(){
        LOGGER.info("Displaying all CardTypes");
        return cardTypeRepo.findAll();
    }

    @PostMapping("")
    public CardType postCardType(@RequestBody CardType ct) {
        
        CardType _cardType = CardType
        .builder()
        .id(ct.getId())
        .name(ct.getName())
        .intrest(ct.getIntrest())
        .build();

        LOGGER.info("Created new Card Type");
        
        return cardTypeRepo.save(_cardType);
    }

    @PutMapping("/{id}")
    public CardType updateCardType(@PathVariable long id, @RequestBody CardType br) {
        
        if(cardTypeRepo.existsById(id)){

            CardType _cdt = cardTypeRepo.findById(id).orElseThrow();
            
            _cdt.setName(br.getName());
            _cdt.setIntrest(br.getIntrest());

            return cardTypeRepo.save(_cdt);

        }
        else{
            LOGGER.info("No CardType with given ID Found");
        }
        return null;
    }

    
    @DeleteMapping("/{id}")
    public String deleteCardType(@PathVariable long id){

        if(cardTypeRepo.existsById(id)){
            LOGGER.info("Deleted CardType");
            cardTypeRepo.deleteById(id);
            return "CardType Deleted Successfully";
        }
        else{
            LOGGER.info("No CardType Found with Given ID");
            return "CardType not Found";
        }
    }

    
}
