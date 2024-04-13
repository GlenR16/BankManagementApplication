package com.wissen.bank.userservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.bank.userservice.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByCustomerId(String customerId);
    public Optional<User> findByEmail(String email);
    public Optional<User> findByPhone(String phone);
    public Optional<User> findByPan(String pan);
    public Optional<User> findByAadhaar(String aadhaar);
}
