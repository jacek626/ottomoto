package com.app.service;

import com.app.entity.User;
import com.app.entity.VerificationToken;
import com.app.utils.EmailMessage;
import com.app.utils.Result;
import com.app.utils.SystemEmail;
import com.app.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Locale;
import java.util.Properties;

@Service("emailService")
public class EmailService {
	private final EmailValidator emailValidator;

	private final MessageSource messageSource;

	private final SystemEmail systemEmail;

	private final VerificationTokenService verificationTokenService;

	public EmailService(EmailValidator emailValidator, MessageSource messageSource, SystemEmail systemEmail, VerificationTokenService verificationTokenService) {
		this.emailValidator = emailValidator;
		this.messageSource = messageSource;
		this.systemEmail = systemEmail;
		this.verificationTokenService = verificationTokenService;
	}

	public Result sendEmail(EmailMessage emailMessage) {
		Result result = emailValidator.checkBeforeSend(emailMessage);

		if (result.isSuccess())
			try {
				Session session = setUpMailSession(setUpSystemMailProperties(), systemEmail.getAddress(), systemEmail.getPassword());
				Transport.send(createMessage(emailMessage, session));
			} catch (MessagingException e) {
                e.printStackTrace();
                return Result.error();
            }

        return result;
    }

    public Result sendEmailWithAccountActivationLink(User user, String appUrl) {
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);

        EmailMessage emailMessage = createActivationEmail(user, appUrl, verificationToken.getToken());

        return sendEmail(emailMessage);
    }

    protected EmailMessage createActivationEmail(User user, String appUrl, String verificationToken) {
        String emailSubject = messageSource.getMessage("activationEmailSubject", new Object[]{}, Locale.getDefault());
        StringBuilder emailText = new StringBuilder(messageSource.getMessage("activationEmailText", new Object[]{}, Locale.getDefault()));
        emailText.append("\n");
        emailText.append(appUrl);
        emailText.append("user/confirmRegistration?token=" + verificationToken);

        return EmailMessage.builder().
                subject(emailSubject).
                content(emailText.toString()).
                senderEmail(systemEmail.getAddress()).
                receiverEmailsAddress(user.getEmail()).
                build();
    }

	private Message createMessage(EmailMessage emailMessage, Session session) throws MessagingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(systemEmail.getAddress()));

		if (emailMessage.getSenderEmail().isPresent())
			message.setReplyTo(new Address[]{new InternetAddress(emailMessage.getSenderEmail().get())});

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", emailMessage.getReceiverEmailsAddresses())));
		message.setSubject(emailMessage.getSubject());
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(emailMessage.getContent(), "text/plain; charset=UTF-8");
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

    protected Properties setUpSystemMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", systemEmail.getMailSmtpHost());
        properties.put("mail.smtp.ssl.trust", systemEmail.getMailSmtpSslTrust());
        properties.put("mail.smtp.port", systemEmail.getMailSmtpPort());
        properties.put("mail.smtp.auth", systemEmail.isMailSmtpAuth());
        properties.put("mail.smtp.starttls.enable", systemEmail.isMailSmtpStarttlsEnabled());

        return properties;
    }

}
