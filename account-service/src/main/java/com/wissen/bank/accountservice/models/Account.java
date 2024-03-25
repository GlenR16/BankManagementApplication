package com.wissen.bank.accountservice.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private long user_id;
    @Column(unique = true)
    private long account_number;
    private String ifsc_code;
    private int type_id;
    private int balance;
    private int withdrawal_limit;

    private boolean is_verified;
    private boolean is_active;
    private boolean is_locked;
    private boolean is_deleted;     //Extra Field

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;
}
