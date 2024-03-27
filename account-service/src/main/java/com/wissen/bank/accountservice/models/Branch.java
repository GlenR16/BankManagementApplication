package com.wissen.bank.accountservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "Branch_Meta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {

    @Id
    private long id;
    private String name;
    private String address;
    private String ifsc_code;
}
