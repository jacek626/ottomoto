package com.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@EnableCaching
public class OtomotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtomotoApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
class StaticResourceConfiguration implements WebMvcConfigurer  {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String myExternalFilePath = "file:///C:/otomoto/";
        registry.addResourceHandler("/images/**").addResourceLocations(myExternalFilePath);
	}

}

@Configuration
class WebMvcConfig implements WebMvcConfigurer {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	  @Bean
	    public LocaleResolver localeResolver() {
	        return new CookieLocaleResolver();
	    }
	    @Bean
	    public LocaleChangeInterceptor localeInterceptor() {
	        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
	        localeInterceptor.setParamName("lang");
	        return localeInterceptor;
	    }
	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(localeInterceptor());
	    }

}
