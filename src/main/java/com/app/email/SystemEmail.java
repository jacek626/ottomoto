package com.app.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SystemEmail {

    @Value("${mail.address}")
    private String address;

    @Value("${mail.pass}")
    private String password;

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${mail.smtp.ssl.trust}")
    private String mailSmtpSslTrust;

    @Value("${mail.smtp.port}")
    private int mailSmtpPort;

    @Value("${mail.smtp.auth}")
    private boolean mailSmtpAuth;

    @Value("${mail.smtp.starttls.enabled}")
    private boolean mailSmtpStarttlsEnabled;
}
