package com.wissen.bank.apigateway.clients;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.wissen.bank.apigateway.models.User;

@HttpExchange
public interface UserClient {

    @PostExchange("/user/verify")
    public User verifyUser(@RequestBody Map<String,String> body);

}
