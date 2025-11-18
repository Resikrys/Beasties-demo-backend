package com.beasties.beasties_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeastiesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeastiesBackendApplication.class, args);
	}

}
