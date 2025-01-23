package org.rebrickable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parts")
public class Part {

    @Id
    private String id;

    private String partNum;
    private String name;
    private String color;
    private int quantity;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPartImgUrl() {
        return partImgUrl;
    }

    public void setPartImgUrl(String partImgUrl) {
        this.partImgUrl = partImgUrl;
    }

    @Override
    public String toString() {
        return "Part{" +
                "partNum='" + partNum + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", quantity=" + quantity +
                ", partImgUrl='" + partImgUrl + '\'' +
                '}';
    }
}

