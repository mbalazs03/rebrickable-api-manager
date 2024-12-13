package org.rebrickable;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RebrickableResponse {
    private int count;
    private String next;
    private String previous;
    @JsonProperty("results")
    private List<Set> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Set> getResults() {
        return results;
    }

    public void setResults(List<Set> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BricksetResponse{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}