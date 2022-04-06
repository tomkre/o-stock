package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        return restTemplate.getForObject("http://organization-service/v1/organizations/{organizationId}",
            Organization.class, organizationId);
    }

}
