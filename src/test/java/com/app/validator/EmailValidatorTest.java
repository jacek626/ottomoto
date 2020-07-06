package com.app.validator;

import com.app.utils.email.EmailMessage;
import com.app.utils.validation.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailValidatorTest {

    @InjectMocks
    private EmailValidator emailValidator;

    @Test
    public void shouldVerifyEmailMessageAsCorrect() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("test@email.com").senderEmail("sender@email.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(0);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecSenderEmailIsNotValid() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("test@email.com").senderEmail("senderemailtest.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecSenderEmailIsNotValid_2() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("test@email.com").senderEmail("sender@.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecSenderEmailIsNotValid_3() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("test@email.com").senderEmail("@mail.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecReceiverEmailIsNotValid() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("receiveremail.com").senderEmail("seder@test.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecReceiverEmailIsNotValid_2() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddresses(List.of("test@.com", "@email.com", "test@email.com")).senderEmail("seder@test.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(2);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements()).contains("test@.com", "@email.com");
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnErrorBecReceiverAndSenderAreNotValid() {
        //given
        EmailMessage emailMessage = EmailMessage.builder().content("content").subject("subject").receiverEmailsAddress("receiveremail.com").senderEmail("sedertest.com").build();

        //when
        Result result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(2);
        assertThat(result.isError()).isTrue();
    }

}
