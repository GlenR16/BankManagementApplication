package com.wissen.bank.transactionservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactionType_Meta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionType {

    @Id
    @GeneratedValue
    private long id;
    private String type;
}
