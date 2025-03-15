package org.rebrickable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetPart {

    @JsonProperty("id")
    private int id;

    @JsonProperty("part")
    private PartDetail part;

    @JsonProperty("color")
    private ColorDetail color;

    @JsonProperty("set_num")
    private String setNum;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("is_spare")
    private boolean isSpare;

    @JsonProperty("element_id")
    private String elementId;

    @JsonProperty("num_sets")
    private int numSets;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public PartDetail getPart() {
        return part;
    }
    public void setPart(PartDetail part) {
        this.part = part;
    }
    public ColorDetail getColor() {
        return color;
    }
    public void setColor(ColorDetail color) {
        this.color = color;
    }
    public String getSetNum() {
        return setNum;
    }
    public void setSetNum(String setNum) {
        this.setNum = setNum;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public boolean isSpare() {
        return isSpare;
    }
    public void setSpare(boolean spare) {
        isSpare = spare;
    }
    public String getElementId() {
        return elementId;
    }
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
    public int getNumSets() {
        return numSets;
    }
    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }

    @Override
    public String toString() {
        return "SetPart{" +
                "id=" + id +
                ", part=" + part +
                ", color=" + color +
                ", setNum='" + setNum + '\'' +
                ", quantity=" + quantity +
                ", isSpare=" + isSpare +
                ", elementId='" + elementId + '\'' +
                ", numSets=" + numSets +
                '}';
    }
}