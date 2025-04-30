package com.strategic.ludo.strategicLudo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.strategic.ludo.strategicLudo", "Controllers", "Services"})
@EntityScan(basePackages = {"com.strategic.ludo.strategicLudo", "Entities"})
@EnableJpaRepositories(basePackages = {"com.strategic.ludo.strategicLudo", "Interfaces"})
public class StrategicLudoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrategicLudoApplication.class, args);
	}

}