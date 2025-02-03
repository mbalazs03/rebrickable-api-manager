package org.rebrickable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PartDetail {

    @JsonProperty("part_num")
    private String partNum;

    @JsonProperty("name")
    private String name;

    @JsonProperty("part_img_url")
    private String partImgUrl;

    public String getPartNum() {
        return partNum;
    }
    public void setPartNum(String partNum) {
        this.partNum = partNum;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPartImgUrl() {
        return partImgUrl;
    }
    public void setPartImgUrl(String partImgUrl) {
        this.partImgUrl = partImgUrl;
    }

    @Override
    public String toString() {
        return "PartDetail{" +
                "partNum='" + partNum + '\'' +
                ", name='" + name + '\'' +
                ", partImgUrl='" + partImgUrl + '\'' +
                '}';
    }
}
