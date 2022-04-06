package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    public Organization getOrganizaton(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");
        log.info("services: {}", discoveryClient.getServices());
        log.info("organization-service instances: {}", instances);
        if (instances.isEmpty()) {
            return null;
        }
        String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);
        log.info("Retrieving organization from URI: {}", serviceUri);
        return restTemplate.getForObject(serviceUri, Organization.class);
    }

}
