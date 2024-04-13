package com.wissen.bank.userservice.dto;

import java.util.Date;

import com.wissen.bank.userservice.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    private String customerId;
    private String name;
    private String email;
   
    private Role role;
    private String phone;
    private String gender;
    private String pan;
    private String aadhaar;
    private String state;
    private String city;
    private String address;
    private int pincode;
    private Date dateOfBirth;

    private boolean isLocked;
    private boolean isDeleted;

    private Date createdAt;
    private Date updatedAt;
}
