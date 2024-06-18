package com.projetoa3.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);

		System.out.print("==========================================================================================\n");
		System.out.print("\n---> API - PROJETO A3 RODANDO COM SUCESSO! <---\n\n");
		System.out.print("PARA ACESSAR  SWAGGER, CLIQUE NO LINK ABAIXO!\n");
		System.out.print("http://localhost:9090/swagger-ui/index.html\n\n");
		System.out.print("==========================================================================================\n");
	}
}
