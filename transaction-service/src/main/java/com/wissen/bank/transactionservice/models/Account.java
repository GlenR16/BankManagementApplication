package com.wissen.bank.transactionservice.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private long id;
    private String customerId;
    private long accountNumber;
    private long branchId;
    private long typeId;
    private double balance;
    private int withdrawalLimit;

    private boolean isVerified;
    private boolean isActive;
    private boolean isLocked;
    private boolean isDeleted;

    private Date createdAt;
    private Date updatedAt;
}
