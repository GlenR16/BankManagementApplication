package com.wissen.bank.transactionservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.models.TransactionType;
import com.wissen.bank.transactionservice.repositories.TransactionTypeRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/transaction/type")
public class TransactionTypeController {

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @GetMapping("")
    public List<TransactionType> getAllTransactionType(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        return transactionTypeRepository.findAll();
    }

    @PostMapping("")
    public TransactionType putTransactionType(@RequestBody TransactionType transactionType, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            TransactionType _transactionType = TransactionType
            .builder()
            .type(transactionType.getType())
            .build();
            return transactionTypeRepository.save(_transactionType);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PutMapping("{id}")
    public TransactionType updateTransactionType(@PathVariable long id, @RequestBody TransactionType transactionType, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            TransactionType _transactionType = transactionTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TransactionType not found"));
            _transactionType.setType(transactionType.getType());
            return transactionTypeRepository.save(_transactionType);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TransactionType> deleteTransactionType(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            TransactionType _transactionType = transactionTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction type not found."));
            transactionTypeRepository.delete(_transactionType);
            return ResponseEntity.ok().body(_transactionType);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this record.");
    }


    @PostConstruct
    public void init() {
        TransactionType transactionType1 = TransactionType.builder()
                .type("NEFT")
                .build();
        if (transactionType1 != null) {
            transactionTypeRepository.save(transactionType1);
        }

        TransactionType transactionType2 = TransactionType.builder()
                .type("Withdraw")
                .build();
        if (transactionType2 != null) {
            transactionTypeRepository.save(transactionType2);
        }

        TransactionType transactionType3 = TransactionType.builder()
                .type("Deposit")
                .build();
        if (transactionType3 != null) {
            transactionTypeRepository.save(transactionType3);
        }

        TransactionType transactionType4 = TransactionType.builder()
                .type("Card")
                .build();
        if (transactionType4 != null) {
            transactionTypeRepository.save(transactionType4);
        }
    }

}
