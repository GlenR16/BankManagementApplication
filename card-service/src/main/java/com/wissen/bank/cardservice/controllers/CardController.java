package com.wissen.bank.cardservice.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.cardservice.dao.CardPinDao;
import com.wissen.bank.cardservice.models.Account;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.Role;
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

    @GetMapping("")
    public List<Card> getAllCards(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            return cardService.getAllCards();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/account/{accountNumber}")
    public List<Card> getCardsByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Account account = accountClientService.getAccountByAccountNumber(accountNumber,customerId,role.toString()).block();
        if (account != null &&(role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customerId))){
            return cardService.getCardsByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
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
            return card;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }
    
    @PostMapping("")
    public Card createCard(@RequestBody Card card,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(),customerId,role.toString()).block();
        if (account != null && account.customerId().equals(customerId)){
            Card _card = cardService.createCard(card);
            return _card;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PostMapping("/{number}")
    public Card postCardLockSwitch(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Card card = cardService.getCardByNumber(number);
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customer,role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accounts.stream().anyMatch(account -> account.accountNumber() == card.getAccountNumber()) ){
            return cardService.lockSwitchCardByNumber(number);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PutMapping("/{number}")
    public Card updateCard(@PathVariable long number, @RequestBody CardPinDao cardPinDao, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        Card card = cardService.getCardByNumber(number);
        List<Account> accounts = accountClientService.getAccountsByCustomerId(customer,role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || accounts.stream().anyMatch(account -> account.accountNumber() == card.getAccountNumber())){
            return cardService.updateCardPin(cardPinDao.oldPin(), cardPinDao.newPin(), number);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @DeleteMapping("/{number}")
    public Card deleteCard(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            Card card = cardService.getCardByNumber(number);
            cardService.deleteCardByNumber(number);
            return card;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this record.");

    }

    @PostMapping("/verify/{number}")
    public Card verifyCard(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            return cardService.verifyCardByNumber(number);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PostMapping("/validate")
    public ResponseEntity<String> verifyCard(@RequestBody Card card,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Card _card = cardService.getCardByNumber(card.getNumber());
        if(!_card.isDeleted() && !_card.isLocked() && _card.getPin() == card.getPin() && _card.getCvv() == card.getCvv() ){
            return ResponseEntity.ok("Card validation successful.");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card validation failed.");
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