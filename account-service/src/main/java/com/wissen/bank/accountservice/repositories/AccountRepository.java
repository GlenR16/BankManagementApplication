package com.wissen.bank.accountservice.repositories;

import com.wissen.bank.accountservice.models.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    public List<Account> findByCustomerId(String customerId);
}
