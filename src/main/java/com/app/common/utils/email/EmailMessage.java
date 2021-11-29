package com.app.common.utils.email;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EmailMessage {
    private String subject;

    private String content;

    private String senderEmail;

    @Singular
    private List<String> receiverEmailsAddresses;
}
