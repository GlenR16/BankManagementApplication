package com.wissen.bank.cardservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Credit_Card_Details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardDetail {
    
    @Id
    @GeneratedValue
    private long id;
    private long cardId;
    private double creditLimit;
    private double creditUsed;
    private int creditTransactions;
}
