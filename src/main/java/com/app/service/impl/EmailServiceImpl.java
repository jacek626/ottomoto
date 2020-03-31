package com.app.service.impl;

import com.app.service.EmailService;
import com.app.utils.EmailMessage;
import com.app.utils.Result;
import com.app.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service("emailService")
public class EmailServiceImpl implements EmailService {
	
	Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private EmailValidator emailValidator;


	@Value("${system.email.address}")
	private String systemEmail;
	@Value("${system.email.address.pass}")
	private String systemEmailPassword;

	private final MessageSource messageSource;


	public EmailServiceImpl(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Result sendEmail(EmailMessage emailMessage) {
		Result result = emailValidator.checkBeforeSend(emailMessage);

		if(result.isSuccess())
			try {
				Session session = setUpMailSession(setUpSystemMailProperties(), systemEmail, systemEmailPassword);
				Transport.send(createMessage(emailMessage ,session));
			} catch (MessagingException e) {
				e.printStackTrace();
				return Result.Error();
			}

		return result;
	}

	private Message createMessage(EmailMessage emailMessage, Session session) throws MessagingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(systemEmail)); 
		
		if(emailMessage.getSenderEmail().isPresent())
			message.setReplyTo(new javax.mail.Address[] {new InternetAddress(emailMessage.getSenderEmail().get())});

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", emailMessage.getReceiverEmailsAddresses())));
		message.setSubject(emailMessage.getSubject());
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(emailMessage.getContent(), "text/html");
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
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		return properties;
	}

}