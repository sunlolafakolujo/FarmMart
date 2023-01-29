package com.logicgate.staticdata;

public enum UnitOfMeasure {
    KG("Kg"),
    LTR("Litre"),
    BAG("Bag"),
    PCS("Pcs"),
    FT("Foot"),
    MTR("Meter"),
    EA("Each"),
    PKT("Packet"),
    SET("Set"),
    PT("Pint"),
    GAL("Gallon"),
    LB("Pound"),
    BUNDLE("Bundle"),
    ROLL("Roll");

    private final String unitOfMeasure;

    UnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    @Override
    public String toString() {
        return "UnitOfMeasure{" +
                "unitOfMeasure='" + unitOfMeasure + '\'' +
                '}';
    }
}
