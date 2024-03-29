package com.rest.mintos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCaching
@EnableCircuitBreaker
@SpringBootApplication
public class MintosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MintosApplication.class, args);
	}

}
