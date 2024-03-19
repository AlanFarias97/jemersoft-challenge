package com.challenge;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.challenge.persistence.model","com.challenge.persistence.repository"
, "com.challenge.config"})
@ComponentScan(basePackages = {"com.challenge.config",
		"com.challenge.controller",
		"com.challenge.core","com.challenge.persistence.model","com.challenge.persistence.repository"})
@OpenAPIDefinition(
		servers = {
				@Server(url = "https://jemersoft-challenge-production.up.railway.app", description = "Default Server URL")
		}
)
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
