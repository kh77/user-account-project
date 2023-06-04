package com.sm.gateway.security.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private JwtUtil jwtUtil;
    private RouteValidator validator;

    public AuthorizationHeaderFilter(JwtUtil jwtUtil, RouteValidator validator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.validator = validator;
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String token = authorizationHeader.replace("Bearer ", "");

                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
                Claims claims = jwtUtil.getClaimsByToken(token);
                String username = claims.getSubject();
                List<String> roles = (List<String>) claims.get("roles");
                request = exchange.getRequest().mutate().header("user_id", username)
                        .header("role", roles.toString()).build();
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}
