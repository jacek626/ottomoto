package com.app.config;


import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DbConfiguration {

	@Profile("prod")
	@Bean
	public String prodDatabaseConnection() {
		
		System.out.println("PROD PROD");
		return "prod";
	}
	
	@Profile("dev")
	@Bean
	public String devDatabaseConnection() {
		
		System.out.println("DEV DEV");
		return "dev";
	}
	
	
	   @Bean
	    @Profile("test")
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("org.h2.Driver");
	        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
	        dataSource.setUsername("sa");
	        dataSource.setPassword("sa");
	 
	        return dataSource;
	    }
	
	@Profile("test")
	@Bean
	public String testDatabaseConnection() {
		
		System.out.println("TEST TEST");
		return "test";
	}

}
