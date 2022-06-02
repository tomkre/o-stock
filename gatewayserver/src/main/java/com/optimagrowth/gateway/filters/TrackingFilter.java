package com.optimagrowth.gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class TrackingFilter implements GlobalFilter {

    private final FilterUtils filterUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (filterUtils.hasCorrelationId(requestHeaders)) {
              log.debug("{} found in tracking filter: {}", FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId(requestHeaders));
        } else {
            String correlationId = generateCorrelationId();
            exchange = filterUtils.setCorrelationId(exchange, correlationId);
            log.debug("{} generated in tracking filter: {}", FilterUtils.CORRELATION_ID, correlationId);
        }
        log.debug("The authentication name from the token is: {}", getUsername(requestHeaders));
        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private String getUsername(HttpHeaders requestHeaders) {
        String username = "";
        if (filterUtils.hasAuthToken(requestHeaders)) {
            String authToken = filterUtils.getAuthToken(requestHeaders).replace("Bearer ", "");
            JSONObject jsonObj = decodeJWT(authToken);
            try {
                username = jsonObj.getString("preferred_username");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        return username;
    }

    private JSONObject decodeJWT(String jwtToken) {
        String[] tokens = jwtToken.split("\\.");
        String base64EncodedBody = tokens[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        JSONObject jsonObject = new JSONObject(body);
        return jsonObject;
    }


}
