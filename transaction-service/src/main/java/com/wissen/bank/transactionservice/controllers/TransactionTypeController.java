package com.wissen.bank.transactionservice.controllers;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.models.TransactionType;
import com.wissen.bank.transactionservice.repositories.TransactionTypeRepository;

import jakarta.ws.rs.NotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/transaction/type")
public class TransactionTypeController {

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<TransactionType> getAllTransactionType() {
        LOGGER.info("Fetched all Transaction Type");
        return transactionTypeRepository.findAll();
    }

    @PostMapping("")
    public TransactionType puTransactionType(@RequestBody TransactionType tr) {

        Random random = new Random();
        TransactionType _transactionType = TransactionType
                .builder()
                .id(random.nextLong(1, 999))
                .type(tr.getType())
                .build();

                if (_transactionType == null){
                    throw new NotFoundException("Transaction Type Object Null");
                }

        LOGGER.info("Created a transaction type record");
        return transactionTypeRepository.save(_transactionType);
    }

    @PutMapping("{id}")
    public TransactionType updateTransactionType(@PathVariable long id, @RequestBody TransactionType transactionType) {

        if (transactionTypeRepository.existsById(id)) {
            TransactionType _transactionType = transactionTypeRepository.findById(id).orElseThrow();

            _transactionType.setType(transactionType.getType());

            LOGGER.info("Update TransactionType success");
            return transactionTypeRepository.save(_transactionType);
        } else
            LOGGER.info("TransactionType with id :" + id + " not found");
        return null;
    }

    @DeleteMapping("{id}")
    public String deleteTransactionType(@PathVariable long id) {

        if (transactionTypeRepository.existsById(id)) {
            LOGGER.info("Delete TransactionType success");
            transactionTypeRepository.deleteById(id);
            return "DELETED SUCCESSFULLY";
        } else
            LOGGER.info("TransactionType with id :" + id + " not found");
            return "Unsuccessfull";
    }

}
