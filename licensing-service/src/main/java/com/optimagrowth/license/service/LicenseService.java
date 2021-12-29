package com.optimagrowth.license.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;

import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LicenseService {
	
	private final MessageSource messageSource;
	
	private final LicenseRepository licenseRepository;
	
	private final ServiceConfig config;

	public List<License> getAll() {
		return StreamSupport
			.stream(licenseRepository.findAll().spliterator(), false)
			.collect(toList());
	}
	
	public License getLicense(String id) {
		License license = licenseRepository.findById(id).get();
		if (license == null) {
			throw new IllegalArgumentException(messageSource.getMessage
				("license.search.error.message", new Object[] { id }, null));
		}
		return license.withComment(config.getProperty());
	}
	
	public License createLicense(License license) {
		license.setId(UUID.randomUUID().toString());
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}
	
	public License updateLicense(License license) {
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}
	
	public String deleteLicense(String licenseId) {
		licenseRepository.deleteById(licenseId);
		return messageSource.getMessage("license.delete.message", new Object[] { licenseId }, null);
	}

}
