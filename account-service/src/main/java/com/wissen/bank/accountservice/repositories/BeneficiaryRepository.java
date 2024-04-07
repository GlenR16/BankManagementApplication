package com.wissen.bank.accountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.bank.accountservice.models.Beneficiary;
import java.util.List;


@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    public List<Beneficiary> findByAccountNumber(long accountNumber);
}
