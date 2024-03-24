package com.wissen.bank.userservice.models;

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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String customerId;
    private String name;
    @Column(unique = true)
    private String email;
   
    private String password;
    private Role role;
    @Column(unique = true)
    private String phone;
    private String gender;
    @Column(unique = true)
    private String pan;
    @Column(unique = true)
    private String aadhaar;
    private String state;
    private String city;
    private String address;
    private int pincode;
    private Date date_of_birth;

    private boolean is_locked;
    private boolean is_deleted;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    
}
