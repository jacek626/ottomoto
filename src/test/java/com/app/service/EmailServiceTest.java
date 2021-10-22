package com.app.service;

import com.app.common.utils.email.EmailMessage;
import com.app.common.utils.email.SystemEmail;
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
    public void sendEmailWithAccountActivationLinkTest() {
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
	public void shouldReturnSendErrorBecSenderEmailIsWrong()  {
		// given
		EmailMessage emailToSend = EmailMessage.builder().
				subject("subject").
				content("content").
				senderEmail("test.gmail").
				receiverEmailsAddresses(Arrays.asList("test@test.", "@test.com", "testtest.com", "test@testcom", "test@test.com")).
				build();

		//when
		Result sentResult = emailService.sendEmail(emailToSend);

		//then
		assertThat(sentResult.isError()).isTrue();
		assertThat(sentResult.getValidationResult().size()).isEqualTo(2);
		assertThat(sentResult.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(4);
	}



	@Test
	public void shouldReturnBuilderErrorBecReceiverEmailsAddressIsRequired()  {
		// given
		EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
				subject("subject").
				content("content").
				senderEmail("test@test.gmail");

		//then
		assertThrows(IllegalArgumentException.class,() -> {
			emailMessageBuilder.build();
		});
	}

	@Test
	public void shouldReturnBuilderErrorBecSubjectIsRequired()  {
		// given
		EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
				content("content").
				receiverEmailsAddress("test@test.gmail").
				senderEmail("test@test.gmail");

		//then
		assertThrows(IllegalArgumentException.class,() -> {
			emailMessageBuilder.build();
		});
	}

	@Test
	public void shouldReturnBuilderErrorBecContentIsRequired()  {
		// given
		EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
                subject("subject").
                receiverEmailsAddress("test@test.gmail").
                senderEmail("test@test.gmail");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            emailMessageBuilder.build();
        });
    }

    @Test
    public void shouldReturnBuilderErrorBecSenderEmailIsRequired() {
        // given
        EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
                subject("subject").
                content("content").
                receiverEmailsAddress("test@test.gmail");

        //then
        assertThrows(NullPointerException.class, () -> {
            emailMessageBuilder.build();
        });
    }

}
