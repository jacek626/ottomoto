package com.app.service;

import com.app.email.EmailMessage;
import com.app.email.SystemEmail;
import com.app.common.utils.validation.Result;
import com.app.email.EmailService;
import com.app.email.validator.EmailValidator;
import com.app.user.entity.User;
import com.app.verification.VerificationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Locale;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {

    @Mock
    private VerificationTokenService verificationTokenService;

    @Mock
    private MessageSource messageSource;

    @Spy
    private SystemEmail systemEmail;

    @Spy
    private EmailValidator emailValidator;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void createEmailWithAccountActivationLink() {
        //given
        User user = User.builder().email("test@test.com").build();
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn("test");
        when(systemEmail.getAddress()).thenReturn("test@test.pl");

        //when
        EmailMessage activationMessage = emailService.createActivationEmail(user, "appAddress", "token123456");

        //then
        assertThat(activationMessage.getContent()).isNotBlank();
        assertThat(activationMessage.getSubject()).isNotBlank();
    }

	@Test
	public void shouldReturnSendErrorWhenSenderEmailIsWrong()  {
		// given
		EmailMessage emailToSend = EmailMessage.builder().
				subject("subject").
				content("content").
				senderEmail("test.gmail").
				emailReceivers(Arrays.asList("test@test.", "@test.com", "testtest.com", "test@testcom", "test@test.com")).
				build();

		//when
		var sentResult = emailService.sendEmail(emailToSend);

		//then
		assertThat(sentResult.isError()).isTrue();
		assertThat(sentResult.getValidationResult().size()).isEqualTo(2);
		assertThat(sentResult.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(4);
	}

}
