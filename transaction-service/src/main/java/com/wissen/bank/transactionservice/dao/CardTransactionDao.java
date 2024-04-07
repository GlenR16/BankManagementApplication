package com.wissen.bank.transactionservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardTransactionDao {
    private long number;
    private int cvv;
    private int pin;
    private long typeId;
    private long accountNumber;
    private long beneficiaryId;
    private double amount;
}
