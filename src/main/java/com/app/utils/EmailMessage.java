package com.app.utils;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class EmailMessage {
    private @NonNull String subject;
    private @NonNull String content;
    private String senderEmail;

    @NonNull
    @Singular
    private List<String> receiverEmailsAddresses;

    EmailMessage(@NonNull final String subject, @NonNull final String content, final String senderEmail, @NonNull final List<String> receiverEmailsAddresses) {
        if (subject == null) {
            throw new IllegalArgumentException("subject is marked non-null but is null");
        }
        if (content == null) {
            throw new IllegalArgumentException("content is marked non-null but is null");
        }
        if (receiverEmailsAddresses == null) {
            throw new IllegalArgumentException("receiverEmailsAddresses is marked non-null but is null");
        }
        if (receiverEmailsAddresses.isEmpty()) {
            throw new IllegalArgumentException("receiverEmailsAddresses is empty");
        }

        this.subject = subject;
        this.content = content;
        this.senderEmail = senderEmail;
        this.receiverEmailsAddresses = receiverEmailsAddresses;
    }

    public Optional<String> getSenderEmail() {
        return Optional.ofNullable(senderEmail);
    }
}
