package com.logicgate.staticdata;

public enum OrderStatus {
    PAID("Paid"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    ORDERED("Ordered"),
    PLACED("Placed");

    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
