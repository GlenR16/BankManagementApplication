package com.wissen.bank.transactionservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.config.WebClientConfig;
import com.wissen.bank.transactionservice.models.Card;
import com.wissen.bank.transactionservice.models.CreditCardDetail;
import com.wissen.bank.transactionservice.responses.Response;

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
            .bodyToMono(Card.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<CreditCardDetail> getCreditCardDetailByCardId(long cardId, String customerId, String role) {
        return webClientConfig.cardWebClient().get()
            .uri("/card/creditCardDetails/"+cardId)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .retrieve()
            .bodyToMono(CreditCardDetail.class)
            .onErrorResume(e -> Mono.empty());
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
            .bodyToMono(CreditCardDetail.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<Response> verifyCard(Card card, String customerId, String role) {
        return webClientConfig.cardWebClient().put()
            .uri("/card/verify")
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", role);
            })
            .body(Mono.just(card), Card.class)
            .retrieve()
            .bodyToMono(Response.class)
            .onErrorResume(e -> Mono.empty());
    }
}