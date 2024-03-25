package com.wissen.bank.cardservice.controllers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.repositories.CardRepository;

@RestController
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardRepository cardRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public String home(){
        return "Welcome to Card Service";
    }

    @GetMapping("/all")
    public List<Card> getAllCards(){
        LOGGER.info("Getting all Cards");
        return cardRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Card> getCardById(@PathVariable long id){
        LOGGER.info("Getting Card by id: {}",id);
        return cardRepository.findById(id);
    }



    @PostMapping("/login")
    public String loginCard(@RequestBody Card card){
        Optional<Card> _card_ = cardRepository.findByNumber(card.getNumber());
        if(_card_.isPresent()){
            if(_card_.get().getPin()==(card.getPin())){
                LOGGER.info("Logging in card id: {}",_card_.get().getId());
                return "Card Connected";
            }
        }
        return "Invalid Credentials";
    }

    @PostMapping("/register")
    public Card createCard(@RequestBody Card card) throws ParseException{
        Random rand = new Random();
        long cardNo = rand.nextLong(100000000000l,999999999999l); 
        long idNo = rand.nextLong(100000000,999999999);
        int cvv = rand.nextInt(100, 999);
        int pin = rand.nextInt(1000,9999);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR)+5);
        Date exp = c.getTime();
        Card _card = Card
        .builder()
        .account_id(idNo)
        .number(cardNo)
        .cvv(cvv)
        .pin(pin)
        .expiry_date(exp)
        .type_id(card.getType_id())
        .is_verified(true)
        .is_active(true)
        .is_locked(false)
        .is_deleted(false)
        .build();
        cardRepository.save(_card);
        LOGGER.info("Creating card id: {}",_card.getId());
        return _card;
    }

    @PutMapping("/update/{id}")
    public Card updateCard(@PathVariable long id, @RequestBody Card card){
        Optional<Card> _card = cardRepository.findById(id);
        if (_card.isEmpty()){
            return null;
        }
        Card __card = _card.get();
        __card.setPin(card.getPin());
        LOGGER.info("Updating card id: {}",__card.getId());
        return cardRepository.save(__card);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCard(@PathVariable long id){
        LOGGER.info("Deleting card id: {}",id);
        cardRepository.deleteById(id);
        return "Card Deleted";
    }

}