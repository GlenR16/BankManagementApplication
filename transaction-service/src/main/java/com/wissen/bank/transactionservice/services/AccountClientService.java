package com.wissen.bank.transactionservice.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.config.WebClientConfig;
import com.wissen.bank.transactionservice.models.Account;
import com.wissen.bank.transactionservice.models.Beneficiary;

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

    public Mono<Account> updateAccountBalance(long accountNumber, double amount, String customerId, String role) {
        Account account = new Account(0l,customerId,accountNumber,0,0,amount,0,true,true,true,true,new Date(),new Date());
        return webClientConfig.accountWebClient().put()
            .uri("/account/"+accountNumber)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .body(Mono.just(account), Account.class)
            .retrieve()
            .bodyToMono(Account.class);
    }

    public Mono<Beneficiary> getBeneficiaryByBeneficiaryId(long beneficiaryId, String customerId, String role) {
        return webClientConfig.accountWebClient().get()
                .uri("/account/beneficiary/"+beneficiaryId)
                .headers((headers) -> {
                    headers.set("Customer", customerId);
                    headers.set("Role", role);
                })
                .retrieve()
                .bodyToMono(Beneficiary.class);
    }
    
}
