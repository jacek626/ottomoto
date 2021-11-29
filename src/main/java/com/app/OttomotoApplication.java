package com.app;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
public class OttomotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OttomotoApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}


@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
class StaticResourceConfiguration implements WebMvcConfigurer {
    private String s3Location;

    @Value("${amazon.aws.endpointUrl}")
    private String endpointUrl;
    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        s3Location = endpointUrl + "/" + bucketName + "/";
        registry.addResourceHandler("/images/**").addResourceLocations(s3Location);
    }
}

@Configuration
class AWSConfiguration {
	@Value("${cloud.aws.region.static}")
	private String region;

	private String accessKeyId = "AKIAU63H6NLW5S3KACLU";

	private String accessKeySecret = "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";


/*	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials("AKIAU63H6NLW5S3KACLU", "2N+y6SnEf6zjxdg1nPhVr0CSIJhBVR/2+u02xVKv");
		//return new BasicAWSCredentials(System.getenv("S3_KEY"), System.getenv("S3_SECRET"));
	}*/


	@Bean
	public AmazonS3 getAmazonS3Client() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
		// Get Amazon S3 client and return the S3 client object
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
				.withRegion(region)
				.build();
	}

/*
	@Bean
	public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
		builder.setRegion(region);
		AmazonS3 amazonS3 = builder.build();
		return amazonS3;
	}*/
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
