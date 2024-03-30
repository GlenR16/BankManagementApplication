package com.wissen.bank.cardservice.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            throw new IllegalArgumentException("Invalid card");
        }
        return cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
    }

    
    public boolean validateCard(Card card){
        if (card.getType_id() == 0){
            return false;
        }
        return true;
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
            throw new IllegalArgumentException("Invalid card");
        }

        Card _card = Card
        .builder()
        .account_id(makeIdNo())
        .number(makecardNo())
        .cvv(makeCvv())
        .pin(makePin())
        .expiry_date(makeExpiry())
        .type_id(card.getType_id())
        .is_verified(true)
        .is_active(true)
        .is_locked(false)
        .is_deleted(false)
        .build();

        if (_card == null){
            throw new NotFoundException("card Not Found");
        }
        return cardRepository.save(_card);
    }

    public Card updateCard(Card newCard, Long id) {
        if (id == null){
            throw new IllegalArgumentException("Invalid card");
        }
        Card card = cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
        if (card == null){
            throw new NotFoundException("card Not Found");
        }
        if (newCard.getPin()>9999 || newCard.getPin()<1000 ){
            throw new IllegalArgumentException("New Pin should be 4 digit number");
        }
        if (newCard.getPin() > 0){
            card.setPin(newCard.getPin());
        }

        return cardRepository.save(card);
    }

    public Card deleteCardById(long id) {
        Card card = cardRepository.findById(id).orElseThrow(()-> new NotFoundException("card Not Found"));
        card.set_deleted(true);
        return cardRepository.save(card);
    }

    public Card getNumber(long number){
        return cardRepository.findByNumber(number).orElseThrow(()-> new NotFoundException("Card Not Found"));
    }



}
