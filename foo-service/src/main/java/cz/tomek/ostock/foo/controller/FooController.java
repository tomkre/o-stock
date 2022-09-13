package cz.tomek.ostock.foo.controller;

import cz.tomek.ostock.foo.config.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("foo")
@RequiredArgsConstructor
public class FooController {

    private final OAuth2RestTemplate restTemplate;

    private final ServiceConfig serviceConfig;

    @GetMapping
    public Map<String, Object> getFoo() {
        Map<String, Object> barResponse = restTemplate.getForObject(serviceConfig.getBarServiceUri(), Map.class);
        Map<String, Object> fooResponse = new HashMap<>();
        fooResponse.putAll(barResponse);
        fooResponse.put("foo", "foo1");
        return fooResponse;
    }

}
