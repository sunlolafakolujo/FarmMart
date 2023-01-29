package com.logicgate.staticdata;

public enum Rating {
    TERRIBLE("TERRIBLE"),
    POOR("POOR"),
    AVERAGE("AVERAGE"),
    GOOD("GOOD"),
    EXCELLENT("EXCELLENT");

    private final String rating;

    Rating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating='" + rating + '\'' +
                '}';
    }
}
