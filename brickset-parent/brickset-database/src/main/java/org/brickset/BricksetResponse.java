package org.brickset;

import com.fasterxml.jackson.databind.JsonNode;

public class BricksetResponse {
    private String status;
    private String message;
    private JsonNode sets;
    private JsonNode minifigures;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getSets() {
        return sets;
    }

    public void setSets(JsonNode sets) {
        this.sets = sets;
    }

    public JsonNode getMinifigures() {
        return minifigures;
    }

    public void setMinifigures(JsonNode minifigures) {
        this.minifigures = minifigures;
    }

    @Override
    public String toString() {
        return "BricksetResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", sets=" + sets +
                ", minifigures=" + minifigures +
                '}';
    }
}
