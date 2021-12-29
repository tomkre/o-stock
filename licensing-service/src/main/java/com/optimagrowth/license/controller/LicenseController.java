package com.optimagrowth.license.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("v1/organizations/{organizationId}/licenses")
@Slf4j
public class LicenseController {
	
	@Autowired
	private LicenseService licenseService;

	@GetMapping
	public List<License> getAll() {
		return licenseService.getAll();
	}

	@GetMapping("{licenseId}")
	public ResponseEntity<License> getLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
		log.info("getLicense [organizationId={}, id={}]", organizationId, licenseId);
		License license = licenseService.getLicense(licenseId);
		return ResponseEntity.ok(license);
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
	
	@Autowired
	private DatasourceConfig config;
	
	@GetMapping("config")
	public ObjectNode getDatabaseConfig() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		json.put("spring.datasource.url", config.getUrl());
		json.put("spring.datasource.username", config.getUsername());
		json.put("spring.datasource.password", config.getPassword());
		return json;
	}

}
