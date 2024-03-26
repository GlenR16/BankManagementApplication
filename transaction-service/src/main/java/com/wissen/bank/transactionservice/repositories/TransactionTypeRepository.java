package com.wissen.bank.transactionservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.transactionservice.models.TransactionType;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

}
