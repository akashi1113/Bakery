package com.csu.bakery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.csu.bakery.persistence")
@SpringBootApplication
public class BakeryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
    }

}
