package com.pebble.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserApplication.class, args);

		System.out.println("===============================================");
		System.out.println("=========== UserApplication started =========== ");
		System.out.println("===============================================");
	}

}
