package com.logicgate.staticdata;

public enum ProductCondition {
    NEW("New"),
    USED("Used"),
    REFURBISHED("Refurbished"),
    FRESH("Fresh"),
    DRY("Dry");

    private final String productCondition;

    ProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductCondition() {
        return productCondition;
    }

    @Override
    public String toString() {
        return "ProductCondition{" +
                "productCondition='" + productCondition + '\'' +
                '}';
    }
}
