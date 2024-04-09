package com.wissen.bank.cardservice.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wissen.bank.cardservice.exceptions.InvalidDataException;
import com.wissen.bank.cardservice.exceptions.NotFoundException;
import com.wissen.bank.cardservice.exceptions.UnauthorizedException;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.models.CreditCardDetail;
import com.wissen.bank.cardservice.repositories.CardRepository;
import com.wissen.bank.cardservice.repositories.CreditCardDetailRepository;


@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository; 


    @Autowired
    private CreditCardDetailRepository creditCardDetailRepository;

    

    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }

    public Card getCardByNumber(long number){
        return cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card not found"));
    }

    public List<Card> getCardsByAccountNumber(long accountNumber){
        return cardRepository.findByAccountNumber(accountNumber);
    }

    private long makecardNo(){
        Random rand = new Random();
        long cardNo = rand.nextLong(100000000000l,999999999999l); 
        return cardNo;
    }

    private int makeCvv(){
        Random rand = new Random();
        int cvv = rand.nextInt(100, 999); 
        return cvv;
    }

    private int makePin(){
        Random rand = new Random();
        int pin = rand.nextInt(1000,9999);
        return pin;
    }

    private Date makeExpiry(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR)+5);
        Date exp = c.getTime();
        return exp;
    }

    @Transactional
    public Card createCard(Card card) {
        if (card == null || !validateCard(card)){
            throw new InvalidDataException("Invalid card");
        }
        Card _card = Card
            .builder()
            .accountNumber(card.getAccountNumber())
            .number(makecardNo())
            .cvv(makeCvv())
            .pin(makePin())
            .expiryDate(makeExpiry())
            .typeId(card.getTypeId())
            .isVerified(true)
            .isActive(true)
            .isLocked(false)
            .isDeleted(false)
            .build();
        if (_card == null){
            throw new NotFoundException("Card not found");
        }
        if (_card.getTypeId() == 2){
            CreditCardDetail ccd = CreditCardDetail
                .builder()
                .cardId(_card.getId())
                .creditTransactions(0)
                .creditLimit(100000)
                .build();
            creditCardDetailRepository.save(ccd);
        }
        return cardRepository.save(_card);
    }

    @Transactional
    public Card updateCardPin(int oldPin, int newPin , long number) {
        Card card = cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card not found"));
        if (card.getPin() == oldPin && newPin != oldPin && newPin < 9999 && newPin > 1000){
            card.setPin(newPin);
            return cardRepository.save(card);
        }
        throw new UnauthorizedException("Invalid pin");
    }

    @Transactional
    public Card lockSwitchCardByNumber(long number) {
        Card card = cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card not found"));
        if (card.isLocked() && card.getUpdatedAt().before(DateUtils.addDays(new Date(), -2))){
            card.setLocked(false);
        }
        else{
            card.setLocked(true);
        }
        return cardRepository.save(card);
    }
    
    @Transactional
    public Card deleteCardByNumber(long number) {
        Card card = cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card not found"));
        card.setDeleted(true);
        return cardRepository.save(card);
    }

    public boolean validateCard(Card card){
        if (card.getTypeId() <= 0 || card.getAccountNumber() <= 0){
            return false;
        }
        return true;
    }

}
