package com.sparity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TemparatureCurdApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemparatureCurdApplication.class, args);
	}
	@Bean
	public RestTemplate rt(RestTemplateBuilder builder) {
		return builder.build();
	}
}
