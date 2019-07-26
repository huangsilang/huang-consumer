package com.example.huang.huangconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HuangConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangConsumerApplication.class, args);
    }

}
