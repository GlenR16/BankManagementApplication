package com.wissen.bank.transactionservice.models;

import java.util.Date;

public record Card(long id,long accountNumber,long number,int cvv,int pin,Date expiryDate,long typeId,boolean isVerified,boolean isActive,boolean isLocked,boolean isDeleted,Date createdAt,Date updatedAt){

}
