package com.app.common.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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

@Configuration
class MyWebApplicationInitializer
        implements WebApplicationInitializer {

    @Autowired
    private ConfigurableEnvironment env;


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        env.setActiveProfiles("someProfile");
        servletContext.setInitParameter(
                "spring.profiles.active", "dev");
    }
}
