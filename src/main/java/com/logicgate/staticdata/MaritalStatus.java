package com.logicgate.staticdata;

public enum MaritalStatus {
    SINGLE("Single"),
    MARRIED("Married");

    private final String maritalStatus;

    MaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    @Override
    public String toString() {
        return "MaritalStatus{" +
                "maritalStatus='" + maritalStatus + '\'' +
                '}';
    }
}
