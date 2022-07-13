package com.optimagrowth.organization.service;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.organization.events.source.ActionEnum;
import com.optimagrowth.organization.events.source.SimpleSourceBean;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    private final SimpleSourceBean simpleSourceBean;

    private final Tracer tracer;

    public List<Organization> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Organization> findById(String id) {
        ScopedSpan newSpan = tracer.startScopedSpan("getOrgDbCall");
        try {
            simpleSourceBean.publishOrganizationChange(ActionEnum.GET, id);
            return repository.findById(id);
        } finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
        return organization;
    }

    public void update(Organization organization) {
        repository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.UPDATED, organization.getId());
    }

    public void delete(String id) {
        repository.deleteById(id);
        simpleSourceBean.publishOrganizationChange(ActionEnum.DELETED, id);
    }

}
