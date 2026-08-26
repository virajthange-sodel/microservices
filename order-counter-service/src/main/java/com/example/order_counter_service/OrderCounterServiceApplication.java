package com.example.order_counter_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderCounterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderCounterServiceApplication.class, args);
	}

}