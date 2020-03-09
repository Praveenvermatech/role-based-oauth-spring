package com.hcl.ecom.rolebasedoauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class RoleBasedOauth2Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RoleBasedOauth2Application.class, args);
	}

}

