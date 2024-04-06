package com.wissen.bank.cardservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.cardservice.models.CreditCardDetail;

public interface CreditCardDetailRepository  extends JpaRepository <CreditCardDetail, Long>{
    public Optional<CreditCardDetail> findByCardId(long cardId);
}
