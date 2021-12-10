package com.app.email;

import com.app.common.utils.validation.Result;
import com.app.email.validator.EmailValidator;
import com.app.user.entity.User;
import com.app.verification.VerificationTokenService;
import com.app.verification.entity.VerificationToken;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService {
	private final EmailValidator emailValidator;
	private final MessageSource messageSource;
	private final SystemEmail systemEmail;
	private final VerificationTokenService verificationTokenService;

	public Result sendEmail(EmailMessage emailMessage) {
        return  emailValidator.checkBeforeSend(emailMessage).ifSuccess(result -> {
            try {
                Session session = setUpMailSession(setUpSystemMailProperties(), systemEmail.getAddress(), systemEmail.getPassword());
                Transport.send(createMessage(emailMessage, session));
                return result;
            } catch (MessagingException e) {
                e.printStackTrace();
                return Result.error();
            }
        });
    }

    public Result sentMessageToSeller(MessageToSellerData messageToSellerData) {
        String emailSubject = messageSource.getMessage("sendMessageToSellerSubject", new Object[]{}, Locale.getDefault());
        StringBuilder emailText = new StringBuilder(messageSource.getMessage("sendMessageToSellerContent", new Object[]{}, Locale.getDefault()));
        emailText.append(messageToSellerData.getRequestUrl()).append(messageToSellerData.getAnnouncementId());
        emailText.append("\n");
        emailText.append(messageToSellerData.getMessageText());

        EmailMessage emailToSend = EmailMessage.builder().
                subject(emailSubject).
                content(emailText.toString()).
                senderEmail(messageToSellerData.getCustomerEmail()).
                emailReceivers(List.of(messageToSellerData.getSellerEmail())).
                build();

        return sendEmail(emailToSend);
    }

    public Result sendEmailWithAccountActivationLink(User user, String appUrl) {
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);
        EmailMessage emailMessage = createActivationEmail(user, appUrl, verificationToken.getToken());

        return sendEmail(emailMessage);
    }

    public EmailMessage createActivationEmail(User user, String appUrl, String verificationToken) {
        String emailSubject = messageSource.getMessage("activationEmailSubject", new Object[]{}, Locale.getDefault());
        StringBuilder emailText = new StringBuilder(messageSource.getMessage("activationEmailText", new Object[]{}, Locale.getDefault()));
        emailText.append("\n");
        emailText.append(appUrl);
        emailText.append("user/confirmRegistration?token=" + verificationToken);

        return EmailMessage.builder().
                subject(emailSubject).
                content(emailText.toString()).
                senderEmail(systemEmail.getAddress()).
                emailReceivers(List.of(user.getEmail())).
                build();
    }

	private Message createMessage(EmailMessage emailMessage, Session session) throws MessagingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(systemEmail.getAddress()));

        message.setReplyTo(new Address[]{new InternetAddress(emailMessage.getSenderEmail())});

        if(StringUtils.isNotBlank(emailMessage.getSenderEmail())) {
            try {
                message.setReplyTo(new Address[]{new InternetAddress(emailMessage.getSenderEmail())});
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", emailMessage.getEmailReceivers())));
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
