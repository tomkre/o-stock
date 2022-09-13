package cz.tomek.ostock.bar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class BarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarServiceApplication.class, args);
	}

}
