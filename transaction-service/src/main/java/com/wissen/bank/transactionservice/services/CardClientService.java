package com.wissen.bank.transactionservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.transactionservice.config.WebClientConfig;
import com.wissen.bank.transactionservice.models.Card;
import com.wissen.bank.transactionservice.models.CardType;
import com.wissen.bank.transactionservice.models.CreditCardDetail;
import com.wissen.bank.transactionservice.models.Role;

import reactor.core.publisher.Mono;

@Service
public class CardClientService {
    
    @Autowired
    private WebClientConfig webClientConfig;

    public Mono<Card> getCardByNumber(long number,String customerId) {
        return webClientConfig.cardWebClient().get()
            .uri("/card/"+number)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", Role.EMPLOYEE.toString());
            })
            .retrieve()
            .bodyToMono(Card.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<CreditCardDetail> getCreditCardDetailByCardId(long cardId, String customerId) {
        return webClientConfig.cardWebClient().get()
            .uri("/card/creditCardDetails/"+cardId)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", Role.EMPLOYEE.toString());
            })
            .retrieve()
            .bodyToMono(CreditCardDetail.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<CreditCardDetail> updateCreditCardDetail(CreditCardDetail creditCardDetail, String customerId) {
        return webClientConfig.cardWebClient().put()
            .uri("/card/creditCardDetails/"+creditCardDetail.id())
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", Role.EMPLOYEE.toString());
            })
            .body(Mono.just(creditCardDetail), CreditCardDetail.class)
            .retrieve()
            .bodyToMono(CreditCardDetail.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<String> verifyCard(Card card, String customerId) {
        return webClientConfig.cardWebClient().put()
            .uri("/card/validate")
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", Role.EMPLOYEE.toString());
            })
            .body(Mono.just(card), Card.class)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.empty());
    }

    public Mono<CardType> getCardTypeById(long id, String customerId) {
        return webClientConfig.cardWebClient().get()
            .uri("/card/type/"+id)
            .headers((headers) -> {
                headers.set("Customer", customerId);
                headers.set("Role", Role.EMPLOYEE.toString());
            })
            .retrieve()
            .bodyToMono(CardType.class)
            .onErrorResume(e -> Mono.empty());
    }
}
