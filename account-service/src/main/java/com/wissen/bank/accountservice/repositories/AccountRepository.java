package com.wissen.bank.accountservice.repositories;

import com.wissen.bank.accountservice.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
