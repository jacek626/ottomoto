package com.app.validator;

import com.app.utils.Result;
import com.app.utils.ValidationDetails;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;

public interface ValidatorCommonMethods<T> {
    Result checkBeforeSave(T objectToValidate);

    Result checkBeforeDelete(T objectToValidate);

    default Result checkBeforeDeactivate(T objectToValidate) throws Exception {
        throw new NotImplementedException("Not implemented");
    }

    default HashMap<String, ValidationDetails> createErrorMap() {
        return new HashMap<String, ValidationDetails>();
    }
}
