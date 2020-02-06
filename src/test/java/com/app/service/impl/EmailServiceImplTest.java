package com.app.service.impl;

import static org.junit.Assert.assertNotNull;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.service.impl.EmailServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceImplTest {

	@Autowired
	EmailServiceImpl emailService;
	
	@Test
	public void sendEmail() throws AddressException, MessagingException {
		//emailService.sendEmail("jacekSuper@com.com", "Email title","Email message", "jacek626@gmail.com");
		
		// String senderEmailAddress, String title,String emailText, String... receiversEmailsAddresses) 
		
	//	assertNotNull(emailService);
	}

}
 