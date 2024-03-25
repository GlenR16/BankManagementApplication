package com.wissen.bank.cardservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.cardservice.models.Card;

public interface CardRepository extends JpaRepository<Card,Long> {
    public Optional<Card> findByNumber(long number);
}
