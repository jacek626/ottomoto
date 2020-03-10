package com.app.security.config;

import com.app.handler.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
	 private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	 @Value("${spring.queries.users-query}")
	 private String usersQuery;
	 
	 @Value("${spring.queries.roles-query}")
	 private String rolesQuery;
	 
	// @Autowired
	// private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	 
	 @Bean
	    public AuthenticationSuccessHandler authenticationSuccessHandlerCustomImpl(){
	        return new AuthenticationSuccessHandlerCustomImpl();
	  }
	 
	 
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth)
	            throws Exception {
	        auth.
	        jdbcAuthentication()
	        .usersByUsernameQuery(usersQuery)
	        .authoritiesByUsernameQuery(rolesQuery)
	        .dataSource(dataSource)
	        .passwordEncoder(bCryptPasswordEncoder);
	        
	    }
	    
	    
		@Bean
		public SpringSecurityDialect securityDialect() {
			return new SpringSecurityDialect();
		}
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                	.antMatchers("/**/favicon.ico", "/css/**","/js/**","/images/**").permitAll()
                	.antMatchers("/user/registration", "/user/registrationSuccess","/announcement/show/*","/user/register").permitAll()
                    .antMatchers("/", "/home","/admin/register","/registerAdmin","/register","admin/register","/logout").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                 //   .loginPage("/user/registration")
                    .loginPage("/user/login").failureUrl("/user/login?error=true")
                    .failureHandler(new CustomAuthenticationFailureHandler())
                    .usernameParameter("login")
                  //  .defaultSuccessUrl("/")
                    .successHandler(authenticationSuccessHandlerCustomImpl())
                    .permitAll()
                    .and()
                .logout()
                	.logoutUrl("/logout")
                	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                	.logoutSuccessUrl("/user/login")
                	.permitAll()
                .and()
                .csrf().disable();
        
    }
}
