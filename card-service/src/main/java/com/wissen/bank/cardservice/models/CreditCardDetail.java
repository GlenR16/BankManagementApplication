package com.wissen.bank.cardservice.models;

import jakarta.persistence.Entity;
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
    private long id;
    private long card_id;
    private int credit_limit;
    private int credit_used;
    private int credit_transactions;
}
