package com.wissen.bank.cardservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.wissen.bank.cardservice.models.Account;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.CreditCardDetail;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.repositories.CardRepository;
import com.wissen.bank.cardservice.repositories.CreditCardDetailRepository;
import com.wissen.bank.cardservice.services.AccountClientService;

@RestController
@RequestMapping("/card/creditCardDetails")
public class CreditCardDetailController {

    @Autowired
    private CreditCardDetailRepository creditCardDetailRepo;

    @Autowired
    private AccountClientService accountClientService;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("")
    public List<CreditCardDetail> getAllCreditCardDetails(@RequestHeader("Customer") String customer,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            return creditCardDetailRepo.findAll();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/{cardId}")
    public CreditCardDetail getCreditCardDetailByCardId(@PathVariable long cardId, @RequestHeader("Customer") String customer,@RequestHeader("Role") Role role) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found."));
        CreditCardDetail ccd = creditCardDetailRepo.findByCardId(card.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card details not found."));
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customer)) {
            return ccd;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @PostMapping("")
    public CreditCardDetail postCreditCardDetail(@RequestBody CreditCardDetail ccd, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            CreditCardDetail _CreditCardDetail = CreditCardDetail
                    .builder()
                    .id(ccd.getId())
                    .cardId(ccd.getCardId())
                    .creditLimit(ccd.getCreditLimit())
                    .creditUsed(ccd.getCreditUsed())
                    .creditTransactions(ccd.getCreditTransactions())
                    .build();
            return creditCardDetailRepo.save(_CreditCardDetail);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PutMapping("/{id}")
    public CreditCardDetail updateCardType(@PathVariable long id, @RequestBody CreditCardDetail newCcd, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        CreditCardDetail ccd = creditCardDetailRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card details not found."));
        Card card = cardRepository.findById(ccd.getCardId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found."));
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            if (newCcd.getCreditLimit() != 0) {
                ccd.setCreditLimit(newCcd.getCreditLimit());
            }
            ccd.setCreditUsed(newCcd.getCreditUsed());
            ccd.setCreditTransactions(newCcd.getCreditTransactions());
            return creditCardDetailRepo.save(ccd);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @DeleteMapping("/{id}")
    public CreditCardDetail deleteCardType(@PathVariable long id, @RequestHeader("Customer") String customer,
            @RequestHeader("Role") Role role) {
        CreditCardDetail ccd = creditCardDetailRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card details not found."));
        Card card = cardRepository.findById(ccd.getCardId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found."));
        Account account = accountClientService
                .getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customer)) {
            creditCardDetailRepo.delete(ccd);
            return ccd;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this record.");
    }

}
