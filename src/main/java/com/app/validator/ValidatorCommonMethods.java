package com.app.validator;

import com.app.utils.validation.Result;
import com.app.utils.validation.ValidationDetails;

import java.util.HashMap;

public interface ValidatorCommonMethods<T> {
    Result checkBeforeSave(T objectToValidate);
    Result checkBeforeDelete(T objectToValidate);

    default HashMap<String, ValidationDetails> createErrorMap() {
        return new HashMap<String, ValidationDetails>();
    }
}
