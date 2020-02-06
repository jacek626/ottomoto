package com.app.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler  extends SimpleUrlAuthenticationFailureHandler {

/*	 @Autowired
	    private MessageSource messages;
	 
	    @Autowired
	    private LocaleResolver localeResolver;*/
	 
	    @Override
	    public void onAuthenticationFailure(HttpServletRequest request, 
	      HttpServletResponse response, AuthenticationException exception)
	      throws IOException, ServletException {
	        setDefaultFailureUrl("/login.html?error=true");
	 
	        super.onAuthenticationFailure(request, response, exception);
	 
	   //     Locale locale = localeResolver.resolveLocale(request);
	 
	        String  errorMessage = exception.getMessage();
	        
	  /*      String errorMessage = messages.getMessage("message.badCredentials", null, "message.badCredentials");
	 
	        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
	            errorMessage = messages.getMessage("auth.message.disabled", null, "auth.message.disable ddddddddddd");
	        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
	            errorMessage = messages.getMessage("auth.message.expired", null, "auth.message.expired !!!!!!!!!!");
	        }
	 */
	        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
	    }
}
