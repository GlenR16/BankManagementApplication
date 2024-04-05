package com.wissen.bank.accountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.bank.accountservice.models.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType,Long> {
    
}
