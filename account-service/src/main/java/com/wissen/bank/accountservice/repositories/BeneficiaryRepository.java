package com.wissen.bank.accountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.accountservice.models.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    
}
