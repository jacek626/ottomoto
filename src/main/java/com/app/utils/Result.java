package com.app.utils;

import com.app.enums.ValidatorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {
	enum OperationResult {
		SUCCESS, ERROR;
	}
	
	private OperationResult status;
	
	private Map<String,ValidatorCode> validationResult = new HashMap<>();
	
	private Result(OperationResult operationStatus) {
		this.setStatus(operationStatus);
	}
	
	public static Result Success() {
		return new Result(OperationResult.SUCCESS);
	}
	
	public static Result Error() {
		return new Result(OperationResult.ERROR);
	}

	public static Result error(Map<String,ValidatorCode> validationResult) {
		Result result = new Result(OperationResult.ERROR);
		result.setValidationResult(validationResult);
		
		return result;
	}
	public static Result error(String fieldWithError, ValidatorCode errorDetails) {
		Result result = new Result(OperationResult.ERROR);
		Map<String,ValidatorCode> validationResult = new HashMap<>();
		validationResult.put(fieldWithError, errorDetails);
		result.setValidationResult(validationResult);
		
		return result;
	}
	
	public static Result create(Map<String, ValidatorCode> validationResult) {
		Result result = new Result(validationResult.isEmpty() ? OperationResult.SUCCESS : OperationResult.ERROR);
		result.setValidationResult(validationResult);
		
		return result;
	}

	public void addOtherResult(Result result) {
		this.status = result.isError() ? OperationResult.ERROR : this.status;
		this.getValidationResult().putAll(result.getValidationResult());

	}
	
	public boolean isSuccess() {
		return (status == OperationResult.SUCCESS ? true : false);
	}
	
	public boolean isError() {
		return (status == OperationResult.ERROR ? true : false);
	}

	public void addToValidationResult(String key, ValidatorCode code) {
		validationResult.put(key, code);
	}

	public Map<String,ValidatorCode> getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(Map<String,ValidatorCode> validationResult) {
		this.validationResult = validationResult;
	}

	public void addResults(List<Result> results) {


	}

	public OperationResult getStatus() {
		return status;
	}

	public void setStatus(OperationResult status) {
		this.status = status;
	}

	public void changeStatusToError() {
		this.status = OperationResult.ERROR;
	}

}
