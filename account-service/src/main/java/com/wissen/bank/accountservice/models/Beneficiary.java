package com.wissen.bank.accountservice.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "Beneficiary_Meta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Beneficiary {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private long accountId;
    private long recieverId;
    private String ifsc;
    
}
