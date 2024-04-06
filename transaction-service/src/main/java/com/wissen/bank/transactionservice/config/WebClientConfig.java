package com.wissen.bank.transactionservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Autowired
    private LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction;

    @Bean
    public WebClient accountWebClient(){
        return WebClient.builder().baseUrl("http://account-service").filter(loadBalancedExchangeFilterFunction).build();
    }
    
    @Bean
    public WebClient cardWebClient(){
        return WebClient.builder().baseUrl("http://card-service").filter(loadBalancedExchangeFilterFunction).build();
    }

}
