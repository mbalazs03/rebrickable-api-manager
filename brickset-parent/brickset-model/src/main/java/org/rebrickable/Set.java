package org.rebrickable;

public class Set {
    private int setNum;
    private String name;
    private int year;
    private int numParts;
    private String imageUrl;

    public Set() {
    }

    public Set(int setNum, String name, int year, int numParts, String imageUrl) {
        this.setNum = setNum;
        this.name = name;
        this.year = year;
        this.numParts = numParts;
        this.imageUrl = imageUrl;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
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
                "setNum='" + setNum + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", numParts=" + numParts +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
