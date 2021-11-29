package com.app.common.validator;

import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;

import java.util.HashMap;

public interface ValidatorCommonMethods<T> {
    Result checkBeforeSave(T objectToValidate);
    Result checkBeforeDelete(T objectToValidate);

    default HashMap<String, ValidationDetails> createErrorsContainer() {
        return new HashMap<>();
    }
}
