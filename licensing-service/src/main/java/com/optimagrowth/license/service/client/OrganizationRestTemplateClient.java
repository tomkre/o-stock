package com.optimagrowth.license.service.client;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.OrganizationRedisRepository;
import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationRestTemplateClient {

    private final KeycloakRestTemplate restTemplate;

    private final OrganizationRedisRepository redisRepository;

    private final Tracer tracer;

    public Organization getOrganization(String organizationId) {
        log.debug("In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());
        Organization cachedOrganization = checkRedisCache(organizationId);
        if (cachedOrganization != null) {
            log.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, cachedOrganization);
            return cachedOrganization;
        }
        log.debug("Unable to locate organization from the redis cache: {}.", organizationId);
        Organization freshOrganization = restTemplate.getForObject("http://gateway:8072/organization/v1/organizations/{organizationId}",
            Organization.class, organizationId);
        if (freshOrganization != null) {
            cacheOrganizationObject(freshOrganization);
        }
        return freshOrganization;
    }

    private Organization checkRedisCache(String organizationId) {
        ScopedSpan newSpan = tracer.startScopedSpan("readLicensingDataFromRedis");
        try {
            return redisRepository
                    .findById(organizationId)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}", organizationId, e);
            return null;
        } finally {
            newSpan.tag("peer.service", "redis");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
            log.debug("Adding into cache organization {}", organization);
        } catch (Exception e) {
            log.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), e);
        }
    }

}
