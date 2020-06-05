package com.app.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Result {
	enum OperationResult {
		SUCCESS, ERROR
    }
	
	private OperationResult status;
	private Map<String, ValidationDetails> validationResult = new HashMap<>();

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
	
	public boolean isSuccess() {
		return (status == OperationResult.SUCCESS);
	}
	
	public boolean isError() {
		return (status == OperationResult.ERROR);
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

/*	public Result ifError(Supplier action) {
		if (isError()) {
			action.get();
		}
		return this;
	}*/

	/*public Result ifSuccess2(Supplier<Result> action) {
		if (isSuccess()) {
			return action.get();
		}
		result
	}*/

/*	public void appendValidationResult(String key, ValidatorCode code) {
		if(validationResult.containsKey(key) && !validationResult.get(key).getDetails().isEmpty()) {
			validationResult.get(key).getDetails().addAll(code.getDetails());
		}
		else
			validationResult.put(key, code);

		if(this.status == OperationResult.SUCCESS)
			changeStatusToError();
	}*/

	public Map<String, ValidationDetails> getValidationResult() {
		return validationResult;
	}

	public ValidationDetails getDetail(String key) {
		return validationResult.get(key);
	}

	public void setValidationResult(Map<String, ValidationDetails> validationResult) {
		this.validationResult = validationResult;
	}

	public OperationResult getStatus() {
		return status;
	}

	public void setStatus(OperationResult status) {
		this.status = status;
	}

	private void changeStatusToError() {
		this.status = OperationResult.ERROR;
	}

}
