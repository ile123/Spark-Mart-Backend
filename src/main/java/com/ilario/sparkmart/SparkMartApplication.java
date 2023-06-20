package com.ilario.sparkmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
public class SparkMartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkMartApplication.class, args);
    }

}
