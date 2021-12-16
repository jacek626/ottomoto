package com.app.email;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {

    @Mock
    @SuppressWarnings("unused")
    private VerificationTokenService verificationTokenService;

    @Mock
    private MessageSource messageSource;

    @Spy
    private SystemEmail systemEmail;

    @Spy
    @SuppressWarnings("unused")
    private EmailValidator emailValidator;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void createEmailWithAccountActivationLink() {
        //given
        var user = User.builder().email("test@test.com").build();
        given(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).willReturn("test");
        given(systemEmail.getAddress()).willReturn("test@test.pl");

        //when
        var activationMessage = emailService.createActivationEmail(user, "appAddress", "token123456");

        //then
        assertThat(activationMessage.getContent()).isNotBlank();
        assertThat(activationMessage.getSubject()).isNotBlank();
    }

	@Test
	public void shouldReturnSendErrorWhenSenderEmailIsWrong()  {
		// given
		var emailToSend = EmailMessage.builder().
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
