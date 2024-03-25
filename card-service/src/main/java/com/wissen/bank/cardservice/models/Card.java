package com.wissen.bank.cardservice.models;

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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue
    private long id;
    private long account_id;
    @Column(unique = true)
    private long number;
    private int cvv;
    private int pin;
    
    private Date expiry_date;
    private int type_id;
    private boolean is_verified;
    private boolean is_active;
    private boolean is_locked;
    private boolean is_deleted;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;
}
