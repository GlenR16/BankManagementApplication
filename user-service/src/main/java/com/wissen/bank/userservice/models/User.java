package com.wissen.bank.userservice.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
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
    private Date dateOfBirth;

    private boolean isLocked;
    private boolean isDeleted;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    public boolean changePassword(String oldPassword, String newPassword1, String newPassword2){
        if (!oldPassword.equals(this.password) || !newPassword1.equals(newPassword2)){
            return false;
        }
        this.setPassword(newPassword1);
        return true;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
