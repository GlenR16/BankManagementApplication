package com.wissen.bank.transactionservice.models;

public record Beneficiary(long id, String name, long accountNumber, long recieverNumber, String ifsc ) {
    
}
