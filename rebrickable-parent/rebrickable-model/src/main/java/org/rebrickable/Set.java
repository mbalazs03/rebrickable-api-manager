package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sets")
public class Set {

    @Id
    @JsonProperty("set_num")
    private String setNum;

    @JsonProperty("name")
    private String name;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("theme_id")
    private Integer themeId;

    @JsonProperty("num_parts")
    private Integer numParts;

    @JsonProperty("set_img_url")
    private String setImgUrl;

    @JsonProperty("set_url")
    private String setUrl;

    @JsonProperty("last_modified_dt")
    private String lastModifiedDt;

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

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
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

    public String getSetUrl() {
        return setUrl;
    }

    public void setSetUrl(String setUrl) {
        this.setUrl = setUrl;
    }

    public String getLastModifiedDt() {
        return lastModifiedDt;
    }

    public void setLastModifiedDt(String lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setNum='" + setNum + '\'' +
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