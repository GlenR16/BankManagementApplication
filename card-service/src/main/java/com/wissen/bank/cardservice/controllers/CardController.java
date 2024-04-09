package com.wissen.bank.cardservice.controllers;

import java.util.ArrayList;
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

import com.wissen.bank.cardservice.dao.CardPinDao;
import com.wissen.bank.cardservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.cardservice.exceptions.InvalidCredentialsException;
import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.Account;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.responses.Response;
import com.wissen.bank.cardservice.services.AccountClientService;
import com.wissen.bank.cardservice.services.CardService;

import jakarta.annotation.PostConstruct;



@RestController
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardService cardService;
    
    @Autowired
    private AccountClientService accountClientService;


    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Card> getAllCards(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin {} getting all cards",customerId);
            return cardService.getAllCards();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/account/{accountNumber}")
    public List<Card> getCardsByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Account account = accountClientService.getAccountByAccountNumber(accountNumber,customerId,role.toString()).block();
        if (account != null && (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customerId))){
            LOGGER.info("Getting cards for account number: {}",accountNumber);
            return cardService.getCardsByAccountNumber(accountNumber);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/list")
    public List<Card> getCards(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customerId,role.toString()).block();
        List<Card> cards = new ArrayList<>();
        accounts.stream().forEach(account -> {
            cards.addAll(cardService.getCardsByAccountNumber(account.accountNumber()));
        });
        return cards;
    }
    

    @GetMapping("/{number}")
    public Card getCardByNumber(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        Card card = cardService.getCardByNumber(number);
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customer,role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accounts.stream().anyMatch(account -> account.accountNumber() == card.getAccountNumber())){
            LOGGER.info("Admin {} getting card number: {}",customer,number);
            return cardService.getCardByNumber(number);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public Card createCard(@RequestBody Card card,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(),customerId,role.toString()).block();
        if (account != null && account.customerId().equals(customerId)){
            Card _card = cardService.createCard(card);
            LOGGER.info("Creating card number: {}",_card.getNumber());
            return _card;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/{number}")
    public ResponseEntity<Response> postCardLockSwitch(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Card card = cardService.getCardByNumber(number);
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customer,role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accounts.stream().anyMatch(account -> account.accountNumber() == card.getAccountNumber()) ){
            LOGGER.info("Locking card number: {}",number);
            cardService.lockSwitchCardByNumber(number);
            return ResponseEntity.ok().body(new Response(new Date(),200,"Card status changed successfully","/card/"+number));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{number}")
    public Card updateCard(@PathVariable long number, @RequestBody CardPinDao cardPinDao, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        Card card = cardService.getCardByNumber(number);
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customer,role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accounts.stream().anyMatch(account -> account.accountNumber() == card.getAccountNumber())){
            LOGGER.info("Updating card number: {}",number);
            return cardService.updateCardPin(cardPinDao.oldPin(), cardPinDao.newPin(), number);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Response> deleteCard(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Deleting Card number: {}",number);
            cardService.deleteCardByNumber(number);
            return ResponseEntity.ok().body(new Response(new Date(),200,"Card deleted successfully","/card/"+number));
        }
        throw new UnauthorizedException("Unauthorized");

    }

    @PostMapping("/verify")
    public ResponseEntity<Response> verifyCard(@RequestBody Card card){
        Card _card = cardService.getCardByNumber(card.getNumber());
        if(!_card.isDeleted() && !_card.isLocked() && _card.getPin() == card.getPin() && _card.getCvv() == card.getCvv() ){
            return ResponseEntity.ok().body(new Response(new Date(),200,"Card Verified","/card/verify"));
        }
        LOGGER.error("Invalid credentials for card number: {}",card.getNumber());
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
        .accountNumber(9999999999L)
        .typeId(1)
        .build();
        cardService.createCard(card1);
    }
}