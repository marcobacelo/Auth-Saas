package com.auth.saas.dataplane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.auth.saas")
public class DataPlaneApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPlaneApplication.class, args);
    }
}
