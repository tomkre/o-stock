package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("organization-service")
public interface OrganizationFeignClient {

    @GetMapping("/v1/organizations/{organizationId}")
    Organization getOrganization(@PathVariable String organizationId);

}
