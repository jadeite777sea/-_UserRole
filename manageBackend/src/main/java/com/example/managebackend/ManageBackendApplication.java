package com.example.managebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ManageBackendApplication {

    public static void main(String[] args) {
        System.out.println("hello");
         SpringApplication.run(ManageBackendApplication.class, args);
    }

}
