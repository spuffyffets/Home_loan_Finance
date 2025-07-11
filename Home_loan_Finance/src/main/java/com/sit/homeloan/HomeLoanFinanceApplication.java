package com.sit.homeloan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomeLoanFinanceApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomeLoanFinanceApplication.class, args);
	}

}
