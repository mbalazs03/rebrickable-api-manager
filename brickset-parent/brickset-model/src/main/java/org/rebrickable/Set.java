package org.rebrickable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sets")
public class Set {

    @Id
    private String id;

    private String setNum;
    private String name;
    private Integer year;
    private Integer numParts;
    private String setImgUrl;

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
