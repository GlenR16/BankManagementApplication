package com.wissen.bank.apigateway.filters;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wissen.bank.apigateway.models.User;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>  {
    @Autowired
    private RouteValidator routeValidator;

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || authHeader.isEmpty()) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                String token = authHeader.get(0);
                if (authHeader != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    Map<String, String> body = Map.of("token", token);
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        User user = restTemplate.postForObject("http://localhost:8081/user/verify", body, User.class);
                        System.out.println("User: " + user);
                        if (user == null) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        exchange.getRequest().mutate().header("Customer", user.customerId());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                }
                else {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        });
    }

    public AuthenticationFilter() {
        super(Config.class);
    }
    
}
