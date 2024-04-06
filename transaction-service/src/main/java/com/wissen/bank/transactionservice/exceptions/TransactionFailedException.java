package com.wissen.bank.transactionservice.exceptions;

import com.wissen.bank.transactionservice.models.Transaction;

public class TransactionFailedException extends RuntimeException {
    private Transaction transaction;
    public TransactionFailedException(String message, Transaction transaction) {
        super(message);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
