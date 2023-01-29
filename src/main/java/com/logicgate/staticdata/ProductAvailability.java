package com.logicgate.staticdata;

public enum ProductAvailability {
    IN_STOCK("IN_STOCK"),
    OUT_OF_STOCK("OUT_OF_STOCK");

    private final String productAvailability;

    ProductAvailability(String productAvailability) {
        this.productAvailability = productAvailability;
    }

    public String getProductAvailability() {
        return productAvailability;
    }

    @Override
    public String toString() {
        return "ProductAvailability{" +
                "productAvailability='" + productAvailability + '\'' +
                '}';
    }
}
