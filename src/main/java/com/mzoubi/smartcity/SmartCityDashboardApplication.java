package com.mzoubi.smartcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SmartCityDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCityDashboardApplication.class, args);
	}

}
