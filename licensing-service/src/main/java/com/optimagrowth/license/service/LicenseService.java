package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import com.optimagrowth.license.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseService {
	
	private final MessageSource messageSource;
	
	private final LicenseRepository licenseRepository;

	private final ServiceConfig config;
	
	private final OrganizationDiscoveryClient organizationDiscoveryClient;

	private final OrganizationRestTemplateClient organizationRestTemplateClient;

	private final OrganizationFeignClient organizationFeignClient;

	@CircuitBreaker(name = "organizationService")
	public License getLicense(String licenseId, String organizationId, String clientType) {
		License license = licenseRepository.findById(licenseId).get();
		if (license == null) {
			throw new IllegalArgumentException(messageSource.getMessage
				("license.search.error.message", new Object[] { licenseId, organizationId }, null));
		}
		Organization organization = retrieveOrganizationInfo(organizationId, clientType);
		log.info("Retrieved organization: {}", organization);
		if (organization != null) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}
		return license.withComment(config.getProperty());
	}

//	@CircuitBreaker(name = "organizationService")
	private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
		log.info("Retrieving organization via {} Client", clientType);
		switch (clientType) {
			case "discovery":
				return organizationDiscoveryClient.getOrganizaton(organizationId);
			case "rest":
				return organizationRestTemplateClient.getOrganization(organizationId);
			case "feign":
				return organizationFeignClient.getOrganization(organizationId);
			default:
				throw new IllegalArgumentException(String.format("Client %s is not supported", clientType));
		}
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
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

	@CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@Bulkhead(name = "licenseService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "buildFallbackLicenseList")
	@Retry(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
		log.debug("getLicensesByOrganization Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		try {
			TimeUnit.MILLISECONDS.sleep(1_200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return licenseRepository.findByOrganizationId(organizationId);
	}

	private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License();
		license.setLicenseId("0000000-00-00000");
		license.setOrganizationId(organizationId);
		license.setProductName("Sorry no licensing information currently available");
		fallbackList.add(license);
		return fallbackList;
	}

	private void randomlyRunLong() throws TimeoutException {
		Random rand = new Random();
		int randomNum = rand.nextInt(3) + 1;
//		if (randomNum == 3) sleep();
		sleep();
	}

	private void sleep() throws TimeoutException {
		try {
			Thread.sleep(3_000);
//			throw new TimeoutException();
		} catch (InterruptedException e) {
			log.error("{}", e.getMessage());
		}
	}

}
