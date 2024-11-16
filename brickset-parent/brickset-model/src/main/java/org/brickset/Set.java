package org.brickset;

public class Set {
    private int setId;
    private String name;
    private String year;
    private String theme;
    private int pieces;
    private int minifigs;
    private double rating;
    private int reviewCount;

    // Constructors
    public Set() {}

    public Set(int setId, String name, String year, String theme, int pieces, int minifigs, double rating, int reviewCount) {
        this.setId = setId;
        this.name = name;
        this.year = year;
        this.theme = theme;
        this.pieces = pieces;
        this.minifigs = minifigs;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    // Getters and Setters
    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getMinifigs() {
        return minifigs;
    }

    public void setMinifigs(int minifigs) {
        this.minifigs = minifigs;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setId=" + setId +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", theme='" + theme + '\'' +
                ", pieces=" + pieces +
                ", minifigs=" + minifigs +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
