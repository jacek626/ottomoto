package com.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	//	  Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
	      Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
	/*      String return =  String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
	                      + "<div>Exception Message: <b>%s</b></div><body></html>",
	              statusCode, exception==null? "N/A": exception.getMessage());*/
	      
	   //   model.addAttribute("error", exception.getMessage());
	      
	    //  System.out.println(exception.getMessage());
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
