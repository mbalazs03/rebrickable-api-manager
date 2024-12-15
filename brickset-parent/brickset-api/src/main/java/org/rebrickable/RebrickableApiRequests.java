package org.rebrickable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RebrickableApiRequests {

    private final String API_KEY;
    private final RebrickableClient rebrickableClient;

    @Autowired
    public RebrickableApiRequests(@Value("${REBRICKABLE_API_KEY}") String apiKey,
                                  RebrickableClient rebrickableClient) {
        this.API_KEY = apiKey;
        this.rebrickableClient = rebrickableClient;

        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is missing. Please set REBRICKABLE_API_KEY in your application.properties.");
        }
    }

    public RebrickableResponse getSetByNumber(int setNum) {
        return rebrickableClient.getSetByNumber(setNum, API_KEY);
    }

    public RebrickableResponse searchSets(String query, Integer page, Integer pageSize, String ordering) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be null or empty.");
        }
        return rebrickableClient.searchSets(query, page, pageSize, ordering, API_KEY);
    }

    public RebrickableResponse getAllSets(Integer page, Integer pageSize, String ordering) {
        return rebrickableClient.getAllSets(page, pageSize, ordering, API_KEY);
    }
}