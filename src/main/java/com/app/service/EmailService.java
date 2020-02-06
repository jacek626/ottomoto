package com.app.service;

import javax.mail.Session;

import com.app.utils.Result;

public interface EmailService {
	Result sendEmailFromSystem(String subject, String content, String... receiverEmailAddres) throws Exception;
	Result sendEmail(String subject, String content, Session session, String... receiverEmailAddres) throws Exception;
}
