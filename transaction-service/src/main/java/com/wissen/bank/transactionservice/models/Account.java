package com.wissen.bank.transactionservice.models;

import java.util.Date;

public record Account(long id,String customerId,long accountNumber,long branchId,long typeId,double balance,int withdrawalLimit,boolean isVerified,boolean isActive,boolean isLocked,boolean isDeleted,Date createdAt,Date updatedAt){

}
