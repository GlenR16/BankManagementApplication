package com.wissen.bank.apigateway.filters;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import com.wissen.bank.apigateway.exceptions.UnauthorizedException;
import com.wissen.bank.apigateway.models.User;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter  {
    @Autowired
    private RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new UnauthorizedException("Authorization header is missing");
            }
            List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || authHeader.isEmpty()) {
                throw new UnauthorizedException("Authorization header is missing");
            }
            String token = authHeader.get(0);
            if (authHeader != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Map<String, String> body = Map.of("token", token);
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    User user = restTemplate.postForObject("http://localhost:8081/user/verify", body, User.class);
                    if (user == null){
                        throw new UnauthorizedException("Authorization header is missing");
                    }
                    exchange.getRequest().mutate().header("Customer", user.customerId());
                    exchange.getRequest().mutate().header("Role", user.role());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    throw new UnauthorizedException("Authorization header is missing");
                }
            }
            else {
                throw new UnauthorizedException("Authorization header is missing");
            }
        }
        return chain.filter(exchange);
    }
    
}
