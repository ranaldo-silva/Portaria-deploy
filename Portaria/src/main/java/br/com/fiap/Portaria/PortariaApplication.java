package br.com.fiap.Portaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PortariaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PortariaApplication.class, args);
	}
}
