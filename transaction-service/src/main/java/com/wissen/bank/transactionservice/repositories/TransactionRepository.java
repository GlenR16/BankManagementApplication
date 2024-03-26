package com.wissen.bank.transactionservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.transactionservice.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
