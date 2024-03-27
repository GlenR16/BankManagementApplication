package com.wissen.bank.accountservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.accountservice.models.Branch;

public interface BranchRepository extends JpaRepository<Branch,Long> {

} 
