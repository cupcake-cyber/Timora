package com.timora.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class  AppApplication {
	// run
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
