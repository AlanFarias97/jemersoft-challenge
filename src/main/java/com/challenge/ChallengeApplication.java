package com.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.challenge.persistence.model","com.challenge.persistence.repository"
, "com.challenge.config"})
@ComponentScan(basePackages = {"com.challenge.config", "com.challenge.config.event",
		"com.challenge.controller",
		"com.challenge.core","com.challenge.persistence.model","com.challenge.persistence.repository"})
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
