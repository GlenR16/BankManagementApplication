package com.wissen.bank.transactionservice.responses;

import java.util.Date;
import java.util.List;
import com.wissen.bank.transactionservice.models.Transaction;

public record BillResponse(Date date,int status, List<Transaction> transactions, double total, double interest ){

}