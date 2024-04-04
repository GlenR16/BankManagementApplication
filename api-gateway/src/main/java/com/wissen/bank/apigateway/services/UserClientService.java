package com.wissen.bank.apigateway.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wissen.bank.apigateway.config.WebClientConfig;
import com.wissen.bank.apigateway.models.User;

import reactor.core.publisher.Mono;

@Service
public class UserClientService {
    @Autowired
    private WebClientConfig webClientConfig;

    public Mono<User> verifyUser(Map<String, String> body) {
        return webClientConfig.userWebClient().post()
                .uri("/user/verify")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(User.class);
    }

}
