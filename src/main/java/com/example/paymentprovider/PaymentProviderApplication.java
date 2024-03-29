package com.example.paymentprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
@EnableScheduling
public class PaymentProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentProviderApplication.class, args);
	}

}
