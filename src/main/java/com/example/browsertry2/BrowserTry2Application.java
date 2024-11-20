package com.example.browsertry2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrowserTry2Application {

    public static void main(String[] args) {
        SpringApplication.run(BrowserTry2Application.class, args);
    }

}
