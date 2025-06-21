package com.neuromotion.doctores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients 
@SpringBootApplication
public class MsDoctoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDoctoresApplication.class, args);
	}

}
