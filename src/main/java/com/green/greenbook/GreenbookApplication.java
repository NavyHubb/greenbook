package com.green.greenbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
public class GreenbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenbookApplication.class, args);
    }

}