package cz.tomek.ostock.foo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServiceConfig {

    @Value("${signing.key}")
    private String jwtSigningKey;

    @Value("${bar-service.uri}")
    private String barServiceUri;

}
