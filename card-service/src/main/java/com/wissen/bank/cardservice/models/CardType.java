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
@Table(name= "Card_Type_Meta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardType {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double interest;
    
}
