package com.app.email;

import com.app.email.validator.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    public void beforeEach() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void shouldVerifyEmailMessage() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("test@email.com")).senderEmail("sender@email.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(0);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenSenderEmailIsNotValid() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("test@email.com")).senderEmail("senderemailtest.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenSenderEmailIsNotValid_2() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("test@email.com")).senderEmail("sender@.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenSenderEmailIsNotValid_3() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("test@email.com")).senderEmail("@mail.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("senderAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenReceiverEmailIsNotValid() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("receiveremail.com")).senderEmail("seder@test.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(1);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenReceiverEmailIsNotValid_2() {
        //given
        var emailMessage = EmailMessage.builder().content("content").subject("subject").emailReceivers(of("test@.com", "@email.com", "test@email.com")).senderEmail("seder@test.com").build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(1);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements().size()).isEqualTo(2);
        assertThat(result.getValidationResult().get("receiveAddress").getRelatedElements()).contains("test@.com", "@email.com");
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldReturnValidationErrorWhenReceiverAndSenderAreNotValid() {
        //given
        var emailMessage = EmailMessage.builder()
                .content("content")
                .subject("subject")
                .emailReceivers(of("receiveremail.com"))
                .senderEmail("sedertest.com")
                .build();

        //when
        var result = emailValidator.checkBeforeSend(emailMessage);

        //then
        assertThat(result.getValidationResult().size()).isEqualTo(2);
        assertThat(result.isError()).isTrue();
    }

    @Test
    public void shouldThrowExceptionWhenContentIsNull()  {
        // given
        EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
                subject("subject").
                emailReceivers(of("test@test.gmail")).
                senderEmail("test@test.gmail");

        //then
        assertThrows(NullPointerException.class, emailMessageBuilder::build);
    }

    @Test
    public void shouldThrowExceptionWhenSenderIsNull() {
        // given
        EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
                subject("subject").
                content("content").
                emailReceivers(of("test@test.gmail"));

        //then
        assertThrows(NullPointerException.class, emailMessageBuilder::build);
    }

    @Test
    public void shouldThrowExceptionWhenEmailReceiverIsEmpty() {
        // given
        EmailMessage.EmailMessageBuilder emailMessageBuilder = EmailMessage.builder().
                subject("subject").
                content("content").
                emailReceivers(of(""));

        //then
        assertThrows(NullPointerException.class, emailMessageBuilder::build);
    }
}
