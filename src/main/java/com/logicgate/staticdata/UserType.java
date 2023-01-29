package com.logicgate.staticdata;

public enum UserType {
    EMPLOYEE("Employee"),
    SELLER("Seller"),
    BUYER("Buyer");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "userType='" + userType + '\'' +
                '}';
    }
}
