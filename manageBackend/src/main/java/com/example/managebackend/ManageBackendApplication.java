package com.example.managebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ManageBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageBackendApplication.class, args);
    }

}
