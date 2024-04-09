package com.wissen.bank.userservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPasswordDao {
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
}
