package com.targetindia.EcomStreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.targetindia.EcomStreaming.repository")
@EnableScheduling
@EnableAsync
public class EcomStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomStreamingApplication.class, args);
	}
}