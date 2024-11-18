package org.brickset;

import java.util.List;

public class Set {
    private int setId;
    private String number;
    private String name;
    private String year;
    private String theme;
    private String subtheme;
    private int pieces;
    private int minifigs;
    private double rating;
    private int reviewCount;
    private String imageUrl;
    private List<String> tags;
    private boolean owned;
    private boolean wanted;

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

    public Set(int setId, String number, String name, String year, String theme, String subtheme, int pieces, int minifigs, double rating, int reviewCount, String imageUrl, List<String> tags, boolean owned, boolean wanted) {
        this.setId = setId;
        this.number = number;
        this.name = name;
        this.year = year;
        this.theme = theme;
        this.subtheme = subtheme;
        this.pieces = pieces;
        this.minifigs = minifigs;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.owned = owned;
        this.wanted = wanted;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getSubtheme() {
        return subtheme;
    }

    public void setSubtheme(String subtheme) {
        this.subtheme = subtheme;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isWanted() {
        return wanted;
    }

    public void setWanted(boolean wanted) {
        this.wanted = wanted;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setId=" + setId +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", theme='" + theme + '\'' +
                ", subtheme='" + subtheme + '\'' +
                ", pieces=" + pieces +
                ", minifigs=" + minifigs +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", tags=" + tags +
                ", owned=" + owned +
                ", wanted=" + wanted +
                '}';
    }
}
