package com.optimagrowth.organization.service;

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

    public List<Organization> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Organization> findById(String id) {
        return repository.findById(id);
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        return repository.save(organization);
    }

    public void update(Organization organization) {
        repository.save(organization);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

}
