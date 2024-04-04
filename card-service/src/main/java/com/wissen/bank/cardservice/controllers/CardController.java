package com.wissen.bank.cardservice.controllers;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.cardservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.cardservice.exceptions.InvalidCredentialsException;
import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.responses.Response;
import com.wissen.bank.cardservice.services.CardService;

import jakarta.annotation.PostConstruct;


@RestController
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardService cardService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    // @GetMapping("")
    // public String home(){
    //     return "Welcome to Card Service";
    // }

    @GetMapping("")
    public List<Card> getAllCards(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){

        if (role == Role.ADMIN || role == Role.EMPLOYEE){
        LOGGER.info("Admin {} Getting all cards",customerId);
            return cardService.getAllCards();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable long id, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){

        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER){
        LOGGER.info("User "+customer+" Getting Card id: {}",id);
            return cardService.getCardById(id);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public ResponseEntity<Response> createCard(@RequestBody Card card, @RequestHeader("Role") Role role){
    
        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER){
            Card _card = cardService.createCard(card);
            LOGGER.info("Creating card id: {}",_card.getId());

            return ResponseEntity.ok().body(new Response(new Date(),200,"card created successfully","/card/"));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCard(@PathVariable long id, @RequestBody Card card, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){

        if (role == Role.ADMIN || role == Role.EMPLOYEE || role == Role.USER ){
            LOGGER.info("Updating Cardid: {}",id);
            cardService.updateCard(card, id);
            return ResponseEntity.ok().body(new Response(new Date(),200,"card updated successfully","/card/"+id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCard(@PathVariable long id, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        LOGGER.info("Deleting Card id: {}",id);
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            cardService.deleteCardById(id);
            return ResponseEntity.ok().body(new Response(new Date(),200,"Admin "+customer+" deleted the Card successfully","/Card/"+id));
        }
        else{
            throw new UnauthorizedException("Unauthorized");
        }

    }

    @PostMapping("/verify")
    public ResponseEntity<Response> verifyCard(@RequestBody Card card){
        Card _card = cardService.getNumber(card.getNumber());

        if(!_card.isDeleted() && !_card.isLocked() && _card.getPin() == card.getPin() 
        && _card.getCvv() == card.getCvv() ){

            LOGGER.info("Card Verified id: {}",_card.getId());

            return ResponseEntity.ok().body(new Response(new Date(),200,"Card Verified","/card/verify"));
        }

        LOGGER.error("Invalid credentials for Card: {}",card.getNumber());
        throw new InvalidCredentialsException("Invalid Credentials");
    }



    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Response> handleSQLException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }

    @PostConstruct
    public void init(){
        Card card1 = Card.builder()
        .number(123456781234L)
        .typeId(1)
        .pin(1234)
        .cvv(123)
        .build();
        cardService.createCard(card1);
    }

}