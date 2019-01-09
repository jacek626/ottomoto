package com.otomoto.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

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
	
/*	  @Override
	    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	        auth.inMemoryAuthentication()
	          .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
	          .and()
	          .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
	          .and()
	          .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
	    }
	  
	   @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }*/
	   
	   
	/*   @Bean
	    public UserDetailsService userDetailsService() {
	        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
	        return manager;
	    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                	.antMatchers("/**/favicon.ico", "/css/**","/js/**","/images/**").permitAll()
                	.antMatchers("/customer/registration", "/customer/registrationSuccess").permitAll()
                    .antMatchers("/", "/home","/admin/register","/registerAdmin","/admin/registerAdmin","/register","admin/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                 //   .loginPage("/customer/registration")
                    .loginPage("/customer/login").failureUrl("/customer/login?error=true")
                    .usernameParameter("login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
        
        ///announcement/add
    }
}
