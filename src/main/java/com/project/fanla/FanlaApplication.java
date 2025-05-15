package com.project.fanla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.project.fanla.repository")
@EntityScan(basePackages = "com.project.fanla.entity")
public class FanlaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FanlaApplication.class, args);
	}

}
