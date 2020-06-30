package com.app.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.function.Consumer;

public class Result<E> extends ResultBase {
    @Getter
    @Setter
    private E value;

    private Result(OperationResult operationStatus) {
        this.setStatus(operationStatus);
    }

    public static Result success() {
        return new Result(OperationResult.SUCCESS);
	}
	public static Result error() {
		return new Result(OperationResult.ERROR);
	}

	public static Result create(Map<String, ValidationDetails> validationResult) {
		Result result = new Result(validationResult.isEmpty() ? OperationResult.SUCCESS : OperationResult.ERROR);
		result.setValidationResult(validationResult);
		
		return result;
	}

	public void appendResult(Result result) {
		this.status = result.isError() ? OperationResult.ERROR : this.status;
		this.getValidationResult().putAll(result.getValidationResult());
	}

	public void appendValidationResult(String key, ValidationDetails validationDetails) {
        if (validationResult.containsKey(key) && !validationResult.get(key).getRelatedElements().isEmpty()) {
            validationResult.get(key).getRelatedElements().addAll(validationDetails.getRelatedElements());
        } else {
            validationResult.put(key, validationDetails);
        }

        if (this.status == OperationResult.SUCCESS)
            changeStatusToError();
    }

    public void convertToMvcError(BindingResult bindingResult) {
        validationResult.entrySet().stream()
                .map(e -> new FieldError(e.getValue().getObjectName().orElse(bindingResult.getObjectName()), e.getKey(), e.getValue().getRejectedValue(), false, null, new Object[]{e.getValue().getRejectedValue()}, e.getValue().getValidatorCode().toString()))
                .forEach(e -> bindingResult.addError(e));
    }

    public void ifSuccess(Runnable action) {
        if (isSuccess()) {
            action.run();
        }
    }

    public void ifSuccess(Consumer<Result> action) {
        if (isSuccess()) {
            action.accept(this);
		}
	}

	public void ifError(Consumer<Result> action) {
		if (isError()) {
			action.accept(this);
		}
	}

	public void ifError(Runnable action) {
		if (isError()) {
			action.run();
		}
	}
}
