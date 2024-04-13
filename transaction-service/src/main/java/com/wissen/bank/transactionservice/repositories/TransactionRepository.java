package com.wissen.bank.transactionservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.bank.transactionservice.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public Page<Transaction> findAllByOrderByCreatedAtDesc(PageRequest of);
    public Page<Transaction> findByAccountNumberOrderByCreatedAtDesc(long accountNumber,PageRequest of);
}
