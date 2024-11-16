package org.brickset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        BricksetApiRequests bricksetApiRequests = new BricksetApiRequests();
        BricksetApiService apiService = new BricksetApiService();

        Map<String, String> customParams = new HashMap<>();
        customParams.put("year", "2011");

        String response = bricksetApiRequests.getSet(customParams);

        List<Set> sets = apiService.parseSetsFromJson(response);
        for (Set set : sets) {
            System.out.println(set);
        }
    }
}
