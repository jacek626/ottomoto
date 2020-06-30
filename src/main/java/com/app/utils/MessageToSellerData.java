package com.app.utils;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageToSellerData {
    private String requestUrl;
    private int announcementId;
    private String messageText;
    private String customerEmail;
    private String sellerEmail;
}
