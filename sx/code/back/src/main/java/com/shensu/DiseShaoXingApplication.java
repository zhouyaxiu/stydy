package com.shensu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class DiseShaoXingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiseShaoXingApplication.class, args);
    }

}
