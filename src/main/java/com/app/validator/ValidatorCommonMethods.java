package com.app.validator;

import com.app.utils.Result;

public interface ValidatorCommonMethods<T> {
    Result checkBeforeSave(T objectToValidate);
    Result checkBeforeDelete(T objectToValidate);

    default  Result checkBeforeDeactivate(T objectToValidate) throws Exception {
        throw new  Exception("Not implemented");
    }
}
