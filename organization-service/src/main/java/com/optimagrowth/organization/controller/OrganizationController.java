package com.optimagrowth.organization.controller;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;
import com.optimagrowth.organization.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("v1/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService service;

    @GetMapping
    @RolesAllowed({ "ADMIN", "USER" })
    public List<Organization> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    @RolesAllowed({ "ADMIN", "USER" })
    public ResponseEntity<Organization> getOne(@PathVariable String id) {
        log.info("getOrganization [correlationId={}, id={}]",
            UserContextHolder.getContext().getCorrelationId(), id);
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ "ADMIN", "USER" })
    public void update(@PathVariable String id, @RequestBody Organization organization) {
        service.update(organization);
    }

    @PostMapping
    @RolesAllowed({ "ADMIN", "USER" })
    public ResponseEntity<Organization> save(@RequestBody Organization organization, UriComponentsBuilder uriBuilder) {
        Organization savedOrganization = service.create(organization);
        return ResponseEntity
            .created(uriBuilder.path("v1/organizations/{id}").build(savedOrganization.getId()))
            .build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed("ADMIN")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

}
