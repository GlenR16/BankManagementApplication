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
public class Card {
    private long id;
    private long accountNumber;
    private long number;
    private int cvv;
    private int pin;
    private Date expiryDate;
    private long typeId;

    private boolean isVerified;
    private boolean isActive;
    private boolean isLocked;
    private boolean isDeleted;

    private Date createdAt;
    private Date updatedAt;
}
