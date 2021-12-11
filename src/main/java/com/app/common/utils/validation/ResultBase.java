package com.app.common.utils.validation;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class ResultBase {
    @Setter
    protected OperationResult status;

    protected Map<String, ValidationDetails> validationResult = new HashMap<>();

    public Map<String, ValidationDetails> getValidationResult() {
        return validationResult;
    }

    public boolean isError() {
        return (status == OperationResult.ERROR);
    }

    public ValidationDetails getDetail(String key) {
        return validationResult.get(key);
    }

    public void setValidationResult(Map<String, ValidationDetails> validationResult) {
        this.validationResult = validationResult;
    }

    public boolean isSuccess() {
        return (status == OperationResult.SUCCESS);
    }

    public void changeStatusToError() {
        this.status = OperationResult.ERROR;
    }

    enum OperationResult {
        SUCCESS, ERROR
    }

}
