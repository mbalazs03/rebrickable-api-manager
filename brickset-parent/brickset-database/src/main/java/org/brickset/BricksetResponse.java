package org.brickset;

import java.util.List;

public class BricksetResponse {
    private String status;
    private String message;
    private List<Set> sets;

    // Getters and Setters
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

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    @Override
    public String toString() {
        return "BricksetResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", sets=" + sets +
                '}';
    }
}
