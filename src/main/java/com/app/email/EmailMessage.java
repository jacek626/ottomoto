package com.app.email;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Value
@Builder
public class EmailMessage {
    private String subject;

    @NonNull
    private String content;

    @NonNull
    private String senderEmail;

    @NonNull
    private List<@NotBlank String> emailReceivers;
}
