package com.logicgate.staticdata;

public enum FacilityOwnership {
    OWN("Own"),
    LEASE("Lease");

    private final String facilityOwnership;

    FacilityOwnership(String facilityOwnership) {
        this.facilityOwnership = facilityOwnership;
    }

    public String getFacilityOwnership() {
        return facilityOwnership;
    }

    @Override
    public String toString() {
        return "FacilityOwnership{" +
                "facilityOwnership='" + facilityOwnership + '\'' +
                '}';
    }
}
