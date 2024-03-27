package com.wissen.bank.accountservice.models;
import jakarta.persistence.Entity;
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
    private long id;
    private String name;
    private long account_id;
    private long reciever_id;
    private String ifsc_code;
    
}
