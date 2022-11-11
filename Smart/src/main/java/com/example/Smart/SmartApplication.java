package com.example.Smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Order(67)
public class SmartApplication {

	public static void main(String[] args)
	{
		System.setProperty("spring.devtools.restart.enabled","false");
		SpringApplication.run(SmartApplication.class, args);
		System.out.println("Dhruv Vyas");
	}

}
