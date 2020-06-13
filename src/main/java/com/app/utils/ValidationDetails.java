package com.app.utils;

import com.app.enums.ValidatorCode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationDetails {
    private final ValidatorCode validatorCode;
    private String rejectedValue;
    private List<String> relatedElements = new ArrayList<>();


    public ValidationDetails(ValidatorCode validatorCode) {
        this.validatorCode = validatorCode;
    }

    public static ValidationDetails of(ValidatorCode validatorCode) {
        return new ValidationDetails(validatorCode);
    }

    public static ValidationDetails of(ValidatorCode validatorCode, String rejectedValue) {
        ValidationDetails validationDetails = new ValidationDetails(validatorCode);
        validationDetails.rejectedValue = rejectedValue;

        return validationDetails;
    }

    public ValidationDetails appendDetail(String detail) {
        relatedElements.add(detail);
        return this;
    }

}
