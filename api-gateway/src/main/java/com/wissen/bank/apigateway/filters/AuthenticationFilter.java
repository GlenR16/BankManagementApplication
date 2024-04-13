package com.wissen.bank.apigateway.filters;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.wissen.bank.apigateway.services.UserClientService;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private UserClientService userClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing"));
            }
            List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || authHeader.isEmpty()) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing"));
            }
            String token = authHeader.get(0);
            if (authHeader != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Map<String, String> body = Map.of("token", token);
                return userClient.verifyUser(body)
                        .flatMap(user -> {
                            if (user == null) {
                                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Token"));
                            }
                            exchange.getRequest().mutate().header("Customer", user.customerId());
                            exchange.getRequest().mutate().header("Role", user.role());
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing")));
            } else {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing"));
            }
        }
        return chain.filter(exchange);
    }

}
