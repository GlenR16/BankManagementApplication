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
@RequestMapping("/card/type")
public class CardTypeController {

    @Autowired
    private CardTypeRepository cardTypeRepo;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<CardType> getAllCardTypes(@RequestHeader("Customer") String customer,
            @RequestHeader("Role") Role role) {
        return cardTypeRepo.findAll();
    }

    @PostMapping("")
    public CardType postCardType(@RequestBody CardType cardType, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            CardType _cardType = CardType
                    .builder()
                    .id(cardType.getId())
                    .name(cardType.getName())
                    .interest(cardType.getInterest())
                    .build();
            if (_cardType == null) {
                throw new NotFoundException("Card type object null");
            }
            LOGGER.info("Admin {} Created new card type", customer);
            return cardTypeRepo.save(_cardType);
        }

        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public CardType updateCardType(@PathVariable long id, @RequestBody CardType cardType,
            @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            CardType _cardType = cardTypeRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("CardType not found"));
            if (cardType.getName() != null) {
                _cardType.setName(cardType.getName());
            }
            if (cardType.getInterest() != 0) {
                _cardType.setInterest(cardType.getInterest());
            }
            LOGGER.info("Admin {} Updated CardType with id : ", customer, id);
            return cardTypeRepo.save(_cardType);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public String deleteCardType(@PathVariable long id, @RequestHeader("Customer") String customer,
            @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            CardType cardType = cardTypeRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("CardType not found"));
            cardTypeRepo.delete(cardType);
            LOGGER.info("Admin {} Deleted CardType with id : ", customer, id);
            return "CardType Deleted";
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostConstruct
    public void init() {
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
