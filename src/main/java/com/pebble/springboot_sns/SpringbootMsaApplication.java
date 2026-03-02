package com.pebble.springboot_sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class SpringbootMsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMsaApplication.class, args);
	}

}
