package com.app.enums;

public enum ValidatorCode {

    ALREADY_EXISTS,
    NOT_EXISTS,
    IS_NOT_VALID,
    IS_EMPTY,
    IS_DEACTIVATED,
    IS_NOT_SAME,
    IS_NEGATIVE,
    HAVE_REF_OBJECTS;

    private String details;

    public String getDetails() {
        return details;
    }

    public ValidatorCode setDetails(String details) {
        this.details = details;
        return this;
    }

}
