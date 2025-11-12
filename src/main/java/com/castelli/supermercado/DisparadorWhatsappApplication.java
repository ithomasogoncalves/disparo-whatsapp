package com.castelli.supermercado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DisparadorWhatsappApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisparadorWhatsappApplication.class, args);

        System.out.println("\n--- SERVIDOR DO DISPARADOR CASTELLI INICIADO ---");
        System.out.println("--- Pronto para receber requisições em http://localhost:8080 --- \n");
    }
}