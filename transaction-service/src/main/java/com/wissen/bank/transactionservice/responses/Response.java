package com.wissen.bank.transactionservice.responses;

import java.util.Date;

import com.wissen.bank.transactionservice.models.Transaction;

public record Response(Date timestamp, int status, String message, Transaction transaction) {
}
