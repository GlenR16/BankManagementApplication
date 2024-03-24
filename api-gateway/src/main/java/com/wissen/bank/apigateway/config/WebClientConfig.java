package com.wissen.bank.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.wissen.bank.apigateway.clients.UserClient;

@Configuration
public class WebClientConfig {

    @Autowired
    @NonNull
    private LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction;

    @Bean
    @NonNull
    public WebClient userWebClient(){
        return WebClient
        .builder()
        .baseUrl("http://user-service")
        .filter(loadBalancedExchangeFilterFunction)
        .build();
    }

    @Bean
    public UserClient userClient(){
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(userWebClient()))
        .build();
        return factory.createClient(UserClient.class);
    }
}
