package com.blueride.coreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class CoreServiceApplication {

    private CoreServiceApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreServiceApplication.class, args);
    }

}
