package com.logicgate.staticdata;

public enum BusinessEntityType {
    INDIVIDUAL("Individual"),
    COMPANY("Company");

    private final String businessEntityType;

    BusinessEntityType(String businessEntityType) {
        this.businessEntityType = businessEntityType;
    }

    public String getBusinessEntityType() {
        return businessEntityType;
    }

    @Override
    public String toString() {
        return "BusinessEntityType{" +
                "businessEntityType='" + businessEntityType + '\'' +
                '}';
    }
}
