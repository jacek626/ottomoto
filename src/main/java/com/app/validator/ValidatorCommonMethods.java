package com.app.validator;

import com.app.utils.Result;
import org.apache.commons.lang3.NotImplementedException;

public interface ValidatorCommonMethods<T> {
    Result checkBeforeSave(T objectToValidate);
    Result checkBeforeDelete(T objectToValidate);

    default  Result checkBeforeDeactivate(T objectToValidate) throws Exception {
        throw new NotImplementedException("Not implemented");
    }
}
