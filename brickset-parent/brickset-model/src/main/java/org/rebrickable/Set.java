package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Set {
    @JsonProperty("set_num")
    private int setNum;
    private String name;
    private int year;
    @JsonProperty("theme_id")
    private int themeId;
    @JsonProperty("num_parts")
    private int numParts;
    @JsonProperty("set_img_url")
    private String setImgUrl;
    @JsonProperty("set_url")
    private String setUrl;
    @JsonProperty("last_modified_dt")
    private String lastModifiedDt;

    public int getSetNum() { return setNum; }
    public void setSetNum(int setNum) { this.setNum = setNum; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getThemeId() { return themeId; }
    public void setThemeId(int themeId) { this.themeId = themeId; }

    public int getNumParts() { return numParts; }
    public void setNumParts(int numParts) { this.numParts = numParts; }

    public String getSetImgUrl() { return setImgUrl; }
    public void setSetImgUrl(String setImgUrl) { this.setImgUrl = setImgUrl; }

    public String getSetUrl() { return setUrl; }
    public void setSetUrl(String setUrl) { this.setUrl = setUrl; }

    public String getLastModifiedDt() { return lastModifiedDt; }
    public void setLastModifiedDt(String lastModifiedDt) { this.lastModifiedDt = lastModifiedDt; }

    @Override
    public String toString() {
        return "Set{" +
                "setNum=" + setNum +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", themeId=" + themeId +
                ", numParts=" + numParts +
                ", setImgUrl='" + setImgUrl + '\'' +
                ", setUrl='" + setUrl + '\'' +
                ", lastModifiedDt='" + lastModifiedDt + '\'' +
                '}';
    }
}