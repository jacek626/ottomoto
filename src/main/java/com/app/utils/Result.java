package com.app.utils;

import java.util.HashMap;
import java.util.Map;

public class Result {
	enum OperationResult {
		SUCCESS, ERROR;
	}
	
	private OperationResult status;
	
	private Map<String,String> validationResult;
	
	private Result(OperationResult operationStatus) {
		this.setStatus(operationStatus);
	}
	
	public static Result retunSuccess() {
		return new Result(OperationResult.SUCCESS);
	}
	
	public static Result returnError() {
		return new Result(OperationResult.ERROR);
	}
	
	public static Result error(Map<String,String> validationResult) {
		Result result = new Result(OperationResult.ERROR);
		result.setValidationResult(validationResult);
		
		return result;
	}
	public static Result error(String fieldWithError, String errorDetails) {
		Result result = new Result(OperationResult.ERROR);
		Map<String,String> validationResult = new HashMap<>();
		validationResult.put(fieldWithError, errorDetails);
		result.setValidationResult(validationResult);
		
		return result;
	}
	
	public static Result create(Map<String,String> validationResult) {
		Result result = new Result(validationResult.isEmpty() ? OperationResult.SUCCESS : OperationResult.ERROR);
		result.setValidationResult(validationResult);
		
		return result;
	}
	
	public boolean isSuccess() {
		return (status == OperationResult.SUCCESS ? true : false);
	}
	
	public boolean isError() {
		return (status == OperationResult.ERROR ? true : false);
	}

	public Map<String,String> getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(Map<String,String> validationResult) {
		this.validationResult = validationResult;
	}

	public OperationResult getStatus() {
		return status;
	}

	public void setStatus(OperationResult status) {
		this.status = status;
	}
	
	
	
	
	/*
	 * public static OperationResult newInstance(Result result) { OperationResult
	 * operationResult = new OperationResult(); operationResult.setResult(result);
	 * 
	 * return operationResult; }
	 * 
	 * public static OperationResult newInstance(Map<String,String>
	 * validationResult) { OperationResult operationResult = new OperationResult();
	 * operationResult.setResult(validationResult.isEmpty() ? Result.SUCCESS :
	 * Result.ERROR); operationResult.set
	 * 
	 * return operationResult; }
	 */
	

	
	
}
