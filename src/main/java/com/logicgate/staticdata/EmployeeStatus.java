package com.logicgate.staticdata;

public enum EmployeeStatus {
    REGULAR("Regular"),
    RETIRED("Retired"),
    SUSPENDED("Suspended"),
    RESIGNED("Resigned"),
    SACKED("Sacked"),
    CONTRACT("Contract");

    private final String employeeStatus;

    EmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    @Override
    public String toString() {
        return "EmployeeStatus{" +
                "employeeStatus='" + employeeStatus + '\'' +
                '}';
    }
}
