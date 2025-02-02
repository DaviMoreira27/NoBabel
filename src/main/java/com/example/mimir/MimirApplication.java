package com.example.mimir;

import com.example.mimir.common.config.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
@Import( GlobalExceptionHandler.class)
public class MimirApplication {
    public static void main (String[] args) {
        SpringApplication.run(MimirApplication.class, args);
    }
}
