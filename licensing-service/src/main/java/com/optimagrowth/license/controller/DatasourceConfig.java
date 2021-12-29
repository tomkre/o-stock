package com.optimagrowth.license.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Getter @Setter
public class DatasourceConfig {
	
	private String url;
	
	private String username;
	
	private String password;

}
