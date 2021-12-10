package com.app.common.utils.validation;

import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<E> extends ResultBase {

    @Setter
    private E validatedObject;

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

	public void addValidationResult(String key, ValidationDetails validationDetails) {
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
                .map(e -> new FieldError(e.getValue().getObjectName()
                        .orElse(bindingResult.getObjectName()),
                        e.getKey(), e.getValue().getRejectedValue(), false, null, new Object[]{e.getValue().getRejectedValue()}, e.getValue().getValidatorCode().toString()))
                .forEach(e -> bindingResult.addError(e));
    }

	public Result ifSuccess(Runnable action) {
		if (isSuccess()) {
			action.run();
		}

		return this;
	}

	public Result ifSuccess(Consumer action) {
		if (isSuccess()) {
			action.accept(this);
		}

		return this;
	}

	public Result ifSuccess(Function<Result, Result> action) {
		if (isSuccess()) {
			return action.apply(this);
		}

		return this;
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

	public Result<E> setValidatedObject(E validatedObject) {
		this.validatedObject = validatedObject;
		return this;
	}
}
