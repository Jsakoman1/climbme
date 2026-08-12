package com.climbme.app;

import com.climbme.app.config.OperationalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(OperationalProperties.class)
public class ClimbMeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClimbMeApplication.class, args);
    }
}
