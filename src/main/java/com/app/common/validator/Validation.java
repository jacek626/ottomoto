package com.app.common.validator;

import com.app.common.utils.validation.Result;
import com.app.common.utils.validation.ValidationDetails;

import java.util.HashMap;

public interface Validation<T> {
    Result<T> validateForSave(T objectToValidate);
    Result<T> validateForDelete(T objectToValidate);

    default HashMap<String, ValidationDetails> createErrorsContainer() {
        return new HashMap<>();
    }
}
