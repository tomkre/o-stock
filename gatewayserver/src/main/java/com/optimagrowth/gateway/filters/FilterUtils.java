package com.optimagrowth.gateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";

    public static final String AUTH_TOKEN = "Authorization";

    public static final String USER_ID = "tmx-user-id";

    public static final String ORG_ID = "tmx-org-id";

    public String getCorrelationId(HttpHeaders requestHeaders) {
        return requestHeaders.getFirst(CORRELATION_ID);
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

    public boolean hasCorrelationId(HttpHeaders requestHeaders) {
        return hasRequestHeader(requestHeaders, CORRELATION_ID);
    }

    public String getAuthToken(HttpHeaders requestHeaders) {
        return requestHeaders.getFirst(AUTH_TOKEN);
    }

    public boolean hasAuthToken(HttpHeaders requestHeaders) {
        return requestHeaders.containsKey(AUTH_TOKEN);
    }

    public boolean hasRequestHeader(HttpHeaders requestHeaders, String name) {
        return requestHeaders.containsKey(name);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate()
            .request(
                exchange.getRequest().mutate()
                .header(name, value).build()
            ).build();
    }


}
