package com.app.validator;

import com.app.enums.ValidatorCode;
import com.app.utils.EmailMessage;
import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator  {
	private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

	private static Pattern emailAddressPattern;

	public EmailValidator() {
		emailAddressPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
	}

	public Result checkBeforeSend(EmailMessage emailMessage) {
		Result result = Result.Success();

		for(String emailAddress : emailMessage.getReceiverEmailsAddresses()) {
			if(checkEmailAddressIsNotValid(emailAddress))
				result.appendValidationResult("receiveAddress", ValidationDetails.of(ValidatorCode.IS_NOT_VALID).appendDetail(emailAddress));
		}

		emailMessage.getSenderEmail().filter(this::checkEmailAddressIsNotValid).ifPresent(e -> {
				result.appendValidationResult("senderAddress",  ValidationDetails.of(ValidatorCode.IS_NOT_VALID));
		});

		return result;
	}

	private boolean checkEmailAddressIsNotValid(String emailAddress) {
		return !emailAddressPattern.matcher(emailAddress).matches();
	}


}
