package com.wissen.bank.cardservice.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.cardservice.exceptions.InvalidDataException;
import com.wissen.bank.cardservice.exceptions.NotFoundException;
import com.wissen.bank.cardservice.models.Card;
import com.wissen.bank.cardservice.repositories.CardRepository;


@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;    

    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }

    public Card getCardById(Long id){
        if (id == null){
            throw new InvalidDataException("Invalid card");
        }
        return cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
    }

    
    

    public long makeIdNo(){
        Random rand = new Random();
        long idNo = rand.nextLong(100000000,999999999);
        return idNo;
    }

    public long makecardNo(){
        Random rand = new Random();
        long cardNo = rand.nextLong(100000000000l,999999999999l); 
        return cardNo;
    }

    public int makeCvv(){
        Random rand = new Random();
        int cvv = rand.nextInt(100, 999); 
        return cvv;
    }

    public int makePin(){
        Random rand = new Random();
        int pin = rand.nextInt(1000,9999);
        return pin;
    }

    public Date makeExpiry(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR)+5);
        Date exp = c.getTime();
        return exp;
    }


    public Card createCard(Card card) {

        if (card == null || !validateCard(card)){
            throw new InvalidDataException("Invalid card");
        }

        Card _card = Card
        .builder()
        .customerId(card.getCustomerId())
        .number(makecardNo())
        .cvv(makeCvv())
        .pin(makePin())
        .expiryDate(makeExpiry())
        .typeId(card.getTypeId())
        .isVerified(false)
        .isActive(true)
        .isLocked(false)
        .isDeleted(false)
        .build();

        if (_card == null){
            throw new NotFoundException("card Not Found");
        }
        return cardRepository.save(_card);
    }

    public Card updateCard(Card newCard, Long id) {
        if (id == null){
            throw new InvalidDataException("Invalid card");
        }
        Card card = cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
        if (card == null){
            throw new NotFoundException("card Not Found");
        }
        if (newCard.getPin()>9999 || newCard.getPin()<1000 ){
            throw new InvalidDataException("New Pin should be 4 digit number");
        }
        if (newCard.getPin() > 0){
            card.setPin(newCard.getPin());
        }

        return cardRepository.save(card);
    }

    public Card deleteCardById(long id) {
        Card card = cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
        card.setDeleted(true);
        return cardRepository.save(card);
    }

    public Card getNumber(long number){
        return cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card Not Found"));
    }

    public boolean validateCard(Card card){
        if (card.getTypeId() == 0){
            return false;
        }
        return true;
    }

}
