package com.sm.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserWsApplication.class, args);
	}

}
