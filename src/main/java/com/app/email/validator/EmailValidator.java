package com.app.email.validator;

import com.app.common.types.ValidatorCode;
import com.app.email.EmailMessage;
import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Component
public class EmailValidator {
    private static final String EMAIL_REGEX = "^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    private static final Pattern emailAddressPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public Result checkBeforeSend(EmailMessage emailMessage) {
        isNotEmpty(emailMessage.getSenderEmail());
        isNotEmpty(emailMessage.getEmailReceivers());

        var result = Result.success();

        for (String emailAddress : emailMessage.getEmailReceivers()) {
            if (checkEmailAddressIsInvalid(emailAddress))
                result.addValidationResult("receiveAddress", ValidationDetails.of(ValidatorCode.IS_NOT_VALID).appendDetail(emailAddress));
        }

        if(checkEmailAddressIsInvalid(emailMessage.getSenderEmail())) {
            result.addValidationResult("senderAddress", ValidationDetails.of(ValidatorCode.IS_NOT_VALID).appendDetail(emailMessage.getSenderEmail()));
        }

		return result;
	}

	private boolean checkEmailAddressIsInvalid(String emailAddress) {
		return !emailAddressPattern.matcher(emailAddress).matches();
	}


}
