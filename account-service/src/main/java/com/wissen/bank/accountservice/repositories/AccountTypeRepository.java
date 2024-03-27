package com.wissen.bank.accountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.accountservice.models.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType,Long> {
    
}
