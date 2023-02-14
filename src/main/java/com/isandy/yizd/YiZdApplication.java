package com.isandy.yizd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
public class YiZdApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiZdApplication.class, args);
    }

    @GetMapping("/")
    String string() {
        return "successful";
    }

}
