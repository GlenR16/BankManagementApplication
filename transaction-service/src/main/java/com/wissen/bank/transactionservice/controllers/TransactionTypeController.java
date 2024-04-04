package com.wissen.bank.transactionservice.controllers;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.exceptions.UnauthorizedException;
import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.models.TransactionType;
import com.wissen.bank.transactionservice.repositories.TransactionTypeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;

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

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<TransactionType> getAllTransactionType(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            LOGGER.info("Admin {} Fetched all Transaction Type" ,customerId);
            return transactionTypeRepository.findAll();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("")
    public TransactionType puTransactionType(@RequestBody TransactionType tr, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            Random random = new Random();
            TransactionType _transactionType = TransactionType
                    .builder()
                    .id(random.nextLong(1, 999))
                    .type(tr.getType())
                    .build();

                    if (_transactionType == null){
                        throw new NotFoundException("Transaction Type Object Null");
                    }

            LOGGER.info("Admin {} Created a transaction type record",customerId);
            return transactionTypeRepository.save(_transactionType);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("{id}")
    public TransactionType updateTransactionType(@PathVariable long id, @RequestBody TransactionType transactionType, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if (transactionTypeRepository.existsById(id)) {
                TransactionType _transactionType = transactionTypeRepository.findById(id).orElseThrow();

                _transactionType.setType(transactionType.getType());

                LOGGER.info("Admin {} Update TransactionType success",customerId);
                return transactionTypeRepository.save(_transactionType);
            } else
                LOGGER.info("Admin {} TransactionType with id :" + id + " not found with CUSTOMER : ",customerId);
            return null;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @DeleteMapping("{id}")
    public String deleteTransactionType(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {

        if(role == Role.ADMIN || role == Role.EMPLOYEE)
        {
            if (transactionTypeRepository.existsById(id)) {
                LOGGER.info("Admin {} Delete TransactionType success",customerId);
                transactionTypeRepository.deleteById(id);
                return "DELETED SUCCESSFULLY";
            } else
                LOGGER.info("Admin {} TransactionType with id :" + id + " not found",customerId);
                return "Unsuccessfull";
        }
        throw new UnauthorizedException("Unauthorized");
    }


    @PostConstruct
    public void init() {
        TransactionType transactionType1 = TransactionType.builder()
                .type("Debit")
                .build();
        if (transactionType1 != null) {
            transactionTypeRepository.save(transactionType1);
        }

        TransactionType transactionType2 = TransactionType.builder()
                .type("Credit")
                .build();
        if (transactionType2 != null) {
            transactionTypeRepository.save(transactionType2);
        }
    }

}
