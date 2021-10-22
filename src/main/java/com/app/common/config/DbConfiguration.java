package com.app.common.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DbConfiguration {

    Logger logger = LoggerFactory.getLogger(DbConfiguration.class);

    @Profile("prod")
    @Bean
    public String prodDatabaseConnection() {
        logger.info("USING DATABASE FOR PROD");

        return "prod";
    }

    @Profile("dev")
    @Bean
    public String devDatabaseConnection() {
        logger.info("USING DATABASE FOR DEV");
        return "dev";
    }

    @Profile("test")
    @Bean
    public String testDatabaseConnection() {
        logger.info("USING DATABASE FOR TESTS");

        return "test";
    }

}
