package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Document(collection = "parts")
public class Part {

    @Id
    private String id;

    @NotBlank(message = "Part number cannot be blank")
    @JsonProperty("part_num")
    private String partNum;

    @NotBlank(message = "Name cannot be blank")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Color cannot be blank")
    @JsonProperty("color")
    private String color;

    @Min(value = 0, message = "Quantity cannot be negative")
    @JsonProperty("quantity")
    private int quantity;

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
