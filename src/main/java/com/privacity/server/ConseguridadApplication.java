package com.privacity.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.privacity.server.component.grupo")
//@ComponentScan(basePackages = "com.privacity.server.security")
//@ComponentScan(basePackages = "com.privacity.server.main")
public class ConseguridadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConseguridadApplication.class, args);
	}

}
