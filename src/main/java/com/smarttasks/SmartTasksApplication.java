package com.smarttasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTasksApplication.class, args);
    }
}
