package com.wissen.bank.cardservice.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.Account;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.CreditCardDetail;
import com.wissen.bank.cardservice.models.Role;
import com.wissen.bank.cardservice.repositories.CardRepository;
import com.wissen.bank.cardservice.repositories.CreditCardDetailRepository;
import com.wissen.bank.cardservice.responses.Response;
import com.wissen.bank.cardservice.services.AccountClientService;

import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/card/creditCardDetails")
public class CreditCardDetailController {

    @Autowired
    private CreditCardDetailRepository creditCardDetailRepo;

    @Autowired
    private AccountClientService accountClientService;

    @Autowired
    private CardRepository cardRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<CreditCardDetail> getAllCreditCardDetails(@RequestHeader("Customer") String customer,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} Displaying all CreditCardDetails", customer);
            return creditCardDetailRepo.findAll();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public CreditCardDetail getCreditCardDetailByCardId(@PathVariable long cardId, @RequestHeader("Customer") String customer,@RequestHeader("Role") Role role) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException("Card not found"));
        CreditCardDetail ccd = creditCardDetailRepo.findByCardId(card.getId()).orElseThrow(() -> new NotFoundException("CreditCardDetail not found"));
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customer)) {
            LOGGER.info("User {} displaying CreditCardDetail with card id: {} ", customer, cardId);
            return ccd;
        }
        throw new UnauthorizedException("Unauthorized");
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
            if (_CreditCardDetail == null) {
                throw new NotFoundException("Credit Card Details Object Null");
            }
            LOGGER.info("Admin {} Created new CreditCardDetail", customer);
            return creditCardDetailRepo.save(_CreditCardDetail);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("/{id}")
    public CreditCardDetail updateCardType(@PathVariable long id, @RequestBody CreditCardDetail newCcd, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        CreditCardDetail ccd = creditCardDetailRepo.findById(id).orElseThrow(() -> new NotFoundException("CreditCardDetail not found"));
        Card card = cardRepository.findById(ccd.getCardId()).orElseThrow(() -> new NotFoundException("Card not found"));
        Account account = accountClientService.getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customer)) {
            if (newCcd.getCreditLimit() != 0) {
                ccd.setCreditLimit(newCcd.getCreditLimit());
            }
            if (newCcd.getCreditUsed() != 0) {
                ccd.setCreditUsed(newCcd.getCreditUsed());
            }
            if (newCcd.getCreditTransactions() != 0) {
                ccd.setCreditTransactions(newCcd.getCreditTransactions());
            }
            LOGGER.info("User {} displaying CreditCardDetail with id: {} ", customer, id);
            return creditCardDetailRepo.save(ccd);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCardType(@PathVariable long id, @RequestHeader("Customer") String customer,
            @RequestHeader("Role") Role role) {
        CreditCardDetail ccd = creditCardDetailRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("CreditCardDetail not found"));
        Card card = cardRepository.findById(ccd.getCardId()).orElseThrow(() -> new NotFoundException("Card not found"));
        Account account = accountClientService
                .getAccountByAccountNumber(card.getAccountNumber(), customer, role.toString()).block();
        if (account == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.customerId().equals(customer)) {
            LOGGER.info("User {} displaying CreditCardDetail with id: {}", customer, id);
            return ResponseEntity.ok().body(new Response(new Date(), 200, "Deleted CreditCardDetail successfully","/card/creditCardDetails/" + id));
        }
        throw new UnauthorizedException("Unauthorized");
    }

}
