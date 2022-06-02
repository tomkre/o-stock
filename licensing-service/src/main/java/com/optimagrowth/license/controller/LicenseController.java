package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("v1/organizations/{organizationId}/licenses")
@Slf4j
public class LicenseController {
	
	@Autowired
	private LicenseService licenseService;

	@GetMapping("{licenseId}")
	public License getLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
		log.info("getLicense [organizationId={}, id={}]", organizationId, licenseId);
		return licenseService.getLicense(licenseId, organizationId, "discovery");
	}

	@GetMapping("{licenseId}/{clientType}")
	public License getLicenseWithClient(@PathVariable String organizationId,
										@PathVariable String licenseId,
										@PathVariable String clientType) {
		log.info("getLicenseWithClient [correlationId={}, organizationId={}, id={}]",
				UserContextHolder.getContext().getCorrelationId(), organizationId, licenseId);
		return licenseService.getLicense(licenseId, organizationId, clientType);
	}
	
	@PostMapping
	public ResponseEntity<License> createLicense(@RequestBody License license) {
		log.info("createLicense {}", license);
		return ResponseEntity.ok(licenseService.createLicense(license));
	}
	
	@PutMapping("{licenseId}")
	public ResponseEntity<License> updateLicense(@RequestParam String licenseId, @RequestBody License license) {
		log.info("updateLicense {}", license);
		return ResponseEntity.ok(licenseService.updateLicense(license));
	}
	
	
	@DeleteMapping("{licenseId}")
	public ResponseEntity<String> deleteLicense(@PathVariable String licenseId) {
		log.info("deleteLicense {}", licenseId);
		return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
	}

	@GetMapping
	public List<License> getLicenses(@PathVariable String organizationId) throws TimeoutException {
		log.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return licenseService.getLicensesByOrganization(organizationId);
	}

}
