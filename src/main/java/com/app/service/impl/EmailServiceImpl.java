package com.app.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.app.service.EmailService;
import com.app.utils.Result;

@Service("emailService")
public class EmailServiceImpl implements EmailService {
	
	Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private String systemEmail = "mail.do.testow.app@gmail.com";
	private String systemEmailPassword = "fk34kfj39";

	@Autowired
	MessageSource messageSource;

	
	@Override
	public Result sendEmail(String subject, String content, Session session, String... receiverEmailAddres) throws Exception {
		try {
			Transport.send(createMessage(systemEmail, content, content, session, receiverEmailAddres));
		} catch (AddressException e) {
			logger.error("EMAIL sending error");
			e.printStackTrace();
			
			return Result.returnError();
		}
		
		
		return Result.retunSuccess();
	}
	
	
	@Override
	public Result sendEmailFromSystem(String subject, String content, String... receiverEmailAddres) throws Exception {
		Session session = setUpMailSession(setUpSystemMailProperties(), systemEmail, systemEmailPassword);
		
		return sendEmail(subject, content, session, receiverEmailAddres);
	}

	private Message createMessage(String senderEmailAddress, String subject, String content , Session session, String... receiverEmailAddress)
			throws Exception {
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(systemEmail)); 
		
		if(StringUtils.isNotBlank(senderEmailAddress))
			message.setReplyTo(new javax.mail.Address[] {new InternetAddress(senderEmailAddress)}); 
		
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", receiverEmailAddress)));
		message.setSubject(subject);
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(content, "text/html");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		message.setContent(multipart);
		
		return message;
	}

	private Session setUpMailSession(Properties properties, String email, String password) {
		return Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
	}

	private Properties setUpSystemMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}

}
