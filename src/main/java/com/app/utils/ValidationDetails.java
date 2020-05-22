package com.app.utils;

import com.app.enums.ValidatorCode;

import java.util.ArrayList;
import java.util.List;

public class ValidationDetails {
    private final ValidatorCode validatorCode;
    private List<String> relatedElements = new ArrayList<>();


    public ValidationDetails(ValidatorCode validatorCode) {
        this.validatorCode = validatorCode;
    }

    public static ValidationDetails of(ValidatorCode validatorCode) {
        return new ValidationDetails(validatorCode);
    }

    public static ValidationDetails of(ValidatorCode validatorCode, String relatedElements) {
        ValidationDetails validationDetails = new ValidationDetails(validatorCode);
        validationDetails.relatedElements.add(relatedElements);

        return validationDetails;
    }

    public ValidationDetails appendDetail(String detail) {
        relatedElements.add(detail);
        return this;
    }

    public List<String> getRelatedElements() {
        return relatedElements;
    }

    public ValidationDetails setRelatedElements(List<String> relatedElements) {
        this.relatedElements = relatedElements;
        return this;
    }

    public ValidationDetails appendRelatedElements(String relatedElements) {
        this.relatedElements.add(relatedElements);
        return this;
    }

    public ValidatorCode getCode() {
        return validatorCode;
    }
}
