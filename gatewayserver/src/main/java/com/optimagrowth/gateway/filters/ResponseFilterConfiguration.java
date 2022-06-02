package com.optimagrowth.gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponseFilterConfiguration {

    private final FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = filterUtils.getCorrelationId(request.getHeaders());
            log.debug("Adding the correlation id to the outbound headers. {}", correlationId);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
            log.debug("Completing outgoing request for {}.", request.getURI());
        }));
    }

}
