package com.wissen.bank.cardservice.responses;
import java.util.Date;

public record Response(Date timestamp, int status, String message, String path) {
}