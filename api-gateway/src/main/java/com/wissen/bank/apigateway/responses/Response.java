package com.wissen.bank.apigateway.responses;

import java.util.Date;

public record Response(Date timestamp, int status, String message, String path) {
    
}
