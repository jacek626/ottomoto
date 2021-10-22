package com.app.email.validator;

import com.app.common.enums.ValidatorCode;
import com.app.common.utils.email.EmailMessage;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private static final String EMAIL_REGEX = "^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    private static final Pattern emailAddressPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public Result checkBeforeSend(EmailMessage emailMessage) {
        Result result = Result.success();

        for (String emailAddress : emailMessage.getReceiverEmailsAddresses()) {
            if (checkEmailAddressIsNotValid(emailAddress))
                result.appendValidationResult("receiveAddress", ValidationDetails.of(ValidatorCode.IS_NOT_VALID).appendDetail(emailAddress));
        }

        emailMessage.getSenderEmail().filter(this::checkEmailAddressIsNotValid).ifPresent(e -> {
            result.appendValidationResult("senderAddress", ValidationDetails.of(ValidatorCode.IS_NOT_VALID).appendDetail(e));
        });

		return result;
	}

	private boolean checkEmailAddressIsNotValid(String emailAddress) {
		return !emailAddressPattern.matcher(emailAddress).matches();
	}


}
