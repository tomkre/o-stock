package com.optimagrowth.license.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

    private String server;

    private String port;

}
