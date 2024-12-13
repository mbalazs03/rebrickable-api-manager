package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Set {
    @JsonProperty("set_num")
    private String set_num;

    private String name;

    private int year;

    @JsonProperty("num_parts")
    private int numParts;

    @JsonProperty("set_img_url")
    private String imageUrl;

    public Set() {
    }

    public Set(String set_num, String name, int year, int numParts, String imageUrl) {
        this.set_num = set_num;
        this.name = name;
        this.year = year;
        this.numParts = numParts;
        this.imageUrl = imageUrl;
    }

    public String getSetNum() {
        return set_num;
    }

    public void setSetNum(String set_num) {
        this.set_num = set_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumParts() {
        return numParts;
    }

    public void setNumParts(int numParts) {
        this.numParts = numParts;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Set{" +
                "set_num='" + set_num + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", numParts=" + numParts +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
