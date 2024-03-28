package com.wissen.bank.cardservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.cardservice.models.CardType;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    
}
