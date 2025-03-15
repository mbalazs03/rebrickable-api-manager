package org.rebrickable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColorDetail {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("rgb")
    private String rgb;

    @JsonProperty("is_trans")
    private boolean isTrans;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRgb() {
        return rgb;
    }
    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
    public boolean isTrans() {
        return isTrans;
    }
    public void setTrans(boolean trans) {
        isTrans = trans;
    }

    @Override
    public String toString() {
        return "ColorDetail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rgb='" + rgb + '\'' +
                ", isTrans=" + isTrans +
                '}';
    }
}