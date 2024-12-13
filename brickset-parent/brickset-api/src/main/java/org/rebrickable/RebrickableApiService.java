package org.rebrickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RebrickableApiService {
    private final RebrickableApiRequests rebrickableApiRequests = new RebrickableApiRequests();

    public List<Set> getSets(Map<String, String> params) {
        try {
            RebrickableResponse response = rebrickableApiRequests.searchSets(params.get("search"));

            if (response == null || response.getResults() == null) {
                System.err.println("Error: No sets found or invalid response");
                return new ArrayList<>();
            }

            return response.getResults();

        } catch (Exception e) {
            System.err.println("Error fetching sets: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}