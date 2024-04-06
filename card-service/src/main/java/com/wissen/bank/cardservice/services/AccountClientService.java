package com.wissen.bank.cardservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.cardservice.config.WebClientConfig;
import com.wissen.bank.cardservice.models.Account;

import reactor.core.publisher.Mono;

@Service
public class AccountClientService {
    @Autowired
    private WebClientConfig webClientConfig;

    public Mono<Account> getAccountByAccountNumber(long accountNumber,String customerId, String role) {
        return webClientConfig.accountWebClient().get()
                .uri("/account/"+accountNumber)
                .headers((headers) -> {
                    headers.set("Customer", customerId);
                    headers.set("Role", role);
                })
                .retrieve()
                .bodyToMono(Account.class);
    }

    public Mono<List<Account>> getAccountsByCustomerId(String customerId, String role) {
        return webClientConfig.accountWebClient().get()
            .uri("/account/list")
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .retrieve()
            .bodyToFlux(Account.class)
            .collectList();
    }
    
}
