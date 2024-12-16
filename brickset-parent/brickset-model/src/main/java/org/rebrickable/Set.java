package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Set {
    @JsonProperty("set_num")
    private String setNum;

    @JsonProperty("name")
    private String name;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("num_parts")
    private Integer numParts;

    @JsonProperty("set_img_url")
    private String setImgUrl;

    // Getters and setters
    public String getSetNum() {
        return setNum;
    }

    public void setSetNum(String setNum) {
        this.setNum = setNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNumParts() {
        return numParts;
    }

    public void setNumParts(Integer numParts) {
        this.numParts = numParts;
    }

    public String getSetImgUrl() {
        return setImgUrl;
    }

    public void setSetImgUrl(String setImgUrl) {
        this.setImgUrl = setImgUrl;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setNum='" + setNum + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", numParts=" + numParts +
                ", setImgUrl='" + setImgUrl + '\'' +
                '}';
    }
}
