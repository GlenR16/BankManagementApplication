package com.wissen.bank.transactionservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.bank.transactionservice.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findByAccountNumberOrderByCreatedAtDesc(long accountNumber);
}
