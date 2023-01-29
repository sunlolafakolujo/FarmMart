package com.logicgate.staticdata;

public enum RelationshipWithNextOfKin {
    SPOUSE("Spouse"),
    PARENT("Mother"),
    COUSIN("Cousin"),
    SIBLING("Sibling");

    private final String relationship;

    RelationshipWithNextOfKin(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    @Override
    public String toString() {
        return "RelationShip{" +
                "relationship='" + relationship + '\'' +
                '}';
    }
}
