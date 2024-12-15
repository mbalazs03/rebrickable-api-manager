package org.rebrickable;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RebrickableApiService {
    private final RebrickableApiRequests rebrickableApiRequests;

    public RebrickableApiService(RebrickableApiRequests rebrickableApiRequests) {
        this.rebrickableApiRequests = rebrickableApiRequests;
    }

    public RebrickableResponse getRawResponse(Map<String, String> params) {
        try {
            String search = params.get("search");
            Integer page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : null;
            Integer pageSize = params.containsKey("page_size") ? Integer.parseInt(params.get("page_size")) : null;
            String ordering = params.get("ordering");

            if (search != null && !search.trim().isEmpty()) {
                return rebrickableApiRequests.searchSets(search, page, pageSize, ordering);
            } else {
                return rebrickableApiRequests.getAllSets(page, pageSize, ordering);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching sets: " + e.getMessage());
        }
    }

    public RebrickableResponse getRawResponseByNumber(int setNum) {
        try {
            return rebrickableApiRequests.getSetByNumber(setNum);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching set by number: " + e.getMessage());
        }
    }

    public List<Set> getSets(Map<String, String> params) {
        RebrickableResponse response = getRawResponse(params);
        return response != null ? response.getResults() : new ArrayList<>();
    }

    public Set getSetByNumber(int setNum) {
        RebrickableResponse response = getRawResponseByNumber(setNum);
        return response != null ? response.getResults().get(0) : null;
    }
}