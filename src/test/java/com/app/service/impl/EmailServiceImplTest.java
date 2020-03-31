package com.app.service.impl;

import com.app.utils.EmailMessage;
import com.app.utils.Result;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceImplTest {

	@Autowired
	EmailServiceImpl emailService;

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Test
	public void shouldSendEmail()  {
		// given
		EmailMessage emailToSend = EmailMessage.builder().
				subject("subject").
				content("content").
				senderEmail("test@test.com").
				receiverEmailsAddress("test@test.com").
				build();

		//when
		Result sentResult = emailService.sendEmail(emailToSend);

		//then
		assertThat(sentResult.isSuccess()).isTrue();
	}

	@Test
	public void shouldReturnSendErrorBecSenderEmailIsWrong()  {
		// given
		EmailMessage emailToSend = EmailMessage.builder().
				subject("subject").
				content("content").
				senderEmail("test.gmail").
				receiverEmailsAddresses(Arrays.asList("test@test.","@test.com","testtest.com","test@testcom","test@test.com")).
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

		//when
		//then
		Assertions.assertThrows(IllegalArgumentException.class,() -> {
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

		//when
		//then
		Assertions.assertThrows(NullPointerException.class,() -> {
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

		//when
		//then
		Assertions.assertThrows(NullPointerException.class,() -> {
			emailMessageBuilder.build();
		});
	}

	//@Test
	public void shouldReturnBuilderErrorBecSenderEmailIsRequired()  {
		// given
		EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
				subject("subject").
				content("content").
				receiverEmailsAddress("test@test.gmail");

		//when
		//then
		Assertions.assertThrows(NullPointerException.class,() -> {
			emailMessageBuilder.build();
		});
	}

}
 