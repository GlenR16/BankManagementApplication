package com.wissen.bank.cardservice.models;

import java.util.Date;

public record Account(
    long id,
    String customerId,
    long accountNumber,
    long branchId,
    long typeId,
    int withdrawalLimit,
    boolean isVerified,
    boolean isActive,
    boolean isLocked,
    boolean isDeleted,
    Date createdAt,
    Date updatedAt
) {
    
}
