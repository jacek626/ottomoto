package com.app;

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
public class OtomotoApplication  {

	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder application) { return
	 * application.sources(OtomotoApplication.class); }
	 */
	
	public static void main(String[] args) {
		SpringApplication.run(OtomotoApplication.class, args);
	}
	
}

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
class StaticResourceConfiguration implements WebMvcConfigurer  {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String myExternalFilePath = "file:///C:/Users/Jacek/Documents/otomoto/";
        registry.addResourceHandler("/images/**").addResourceLocations(myExternalFilePath);
	}

}

@Configuration
class WebMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	private ApplicationContext applicationContext;

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

   //     @Override
        public void addResourceHandlers2(ResourceHandlerRegistry registry) {
         //   String myExternalFilePath = "file:///C:/Users/Jacek/Documents/otomoto";
         //   registry.addResourceHandler("/images/**").addResourceLocations(myExternalFilePath);


     /*       registry
                    .addResourceHandler("/images/**")
                    .addResourceLocations("file:///C:/Users/Jacek/Documents/otomoto")
                    .setCachePeriod(3600)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());*/
        }


/*	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		resolver.setContentType("text/html; charset=UTF-8");
		registry.viewResolver(resolver);
	}*/
}