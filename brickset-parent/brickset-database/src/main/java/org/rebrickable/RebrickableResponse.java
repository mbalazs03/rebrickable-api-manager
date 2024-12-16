package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public class RebrickableResponse {
    @JsonProperty("count")
    private Integer count;

    @JsonProperty("results")
    private List<Set> results;

    // Getters and setters
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Set> getResults() {
        return results;
    }

    public void setResults(List<Set> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "RebrickableResponse{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}