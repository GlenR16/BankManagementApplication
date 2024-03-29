package com.wissen.bank.cardservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.cardservice.models.CreditCardDetail;

public interface CreditCardDetailRepository  extends JpaRepository <CreditCardDetail, Long>{

}
