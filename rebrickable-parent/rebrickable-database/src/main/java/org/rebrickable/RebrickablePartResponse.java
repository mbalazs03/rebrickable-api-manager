package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rebrickable.dto.SetPart;

import java.util.List;

public class RebrickablePartResponse {

    @JsonProperty("count")
    private int count;

    @JsonProperty("results")
    private List<SetPart> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SetPart> getResults() {
        return results;
    }

    public void setResults(List<SetPart> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "RebrickablePartResponse{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}
