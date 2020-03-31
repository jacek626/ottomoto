package com.app.service;

import com.app.utils.EmailMessage;
import com.app.utils.Result;

public interface EmailService {
    Result sendEmail(EmailMessage emailMessage);
}
