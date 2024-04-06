package com.wissen.bank.transactionservice.models;

public record CreditCardDetail( long id, long cardId, double creditLimit, double creditUsed, int creditTransactions ) {
    
}
