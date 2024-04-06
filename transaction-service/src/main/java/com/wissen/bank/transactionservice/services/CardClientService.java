package com.wissen.bank.transactionservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.config.WebClientConfig;
import com.wissen.bank.transactionservice.models.Card;
import com.wissen.bank.transactionservice.models.CreditCardDetail;

import reactor.core.publisher.Mono;

@Service
public class CardClientService {
    
    @Autowired
    private WebClientConfig webClientConfig;

    public Mono<Card> getCardByNumber(long number,String customerId, String role) {
        return webClientConfig.cardWebClient().get()
                .uri("/card/"+number)
                .headers((headers) -> {
                    headers.set("Customer", customerId);
                    headers.set("Role", role);
                })
                .retrieve()
                .bodyToMono(Card.class);
    }

    public Mono<CreditCardDetail> getCreditCardDetailByCardId(long cardId, String customerId, String role) {
        return webClientConfig.cardWebClient().get()
            .uri("/card/creditCardDetails/"+cardId)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .retrieve()
            .bodyToMono(CreditCardDetail.class);
        
    }

    public Mono<CreditCardDetail> updateCreditCardDetail(CreditCardDetail creditCardDetail, String customerId, String role) {
        return webClientConfig.cardWebClient().put()
            .uri("/card/creditCardDetails/"+creditCardDetail.id())
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .body(Mono.just(creditCardDetail), CreditCardDetail.class)
            .retrieve()
            .bodyToMono(CreditCardDetail.class);
    }
}
