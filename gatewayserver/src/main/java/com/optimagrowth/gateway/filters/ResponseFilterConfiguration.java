package com.optimagrowth.gateway.filters;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ResponseFilterConfiguration {

    private final FilterUtils filterUtils;

    private final Tracer tracer;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = filterUtils.getCorrelationId(request.getHeaders());
            log.debug("Adding the correlation id to the outbound headers. {}", correlationId);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);

            String traceId = tracer.currentSpan().context().traceIdString();
            log.debug("Adding the traceId id to the outbound headers. {}", traceId);
            exchange.getResponse().getHeaders().add(FilterUtils.TRACE_ID, traceId);

            log.debug("Completing outgoing request for {}.", request.getURI());
        }));
    }

}
