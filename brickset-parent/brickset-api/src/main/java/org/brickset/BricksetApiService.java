package org.brickset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BricksetApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BricksetApiRequests bricksetApiRequests = new BricksetApiRequests();

    public List<Set> getSets(Map<String, String> params) {
        try {
            BricksetResponse response = bricksetApiRequests.getSets(params);
            if (response == null || response.getSets() == null) {
                System.err.println("Error: Received null response or null sets from API");
                return new ArrayList<>();
            }
            return parseSetsFromJson(response.getSets());
        } catch (Exception e) {
            System.err.println("Error fetching sets: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Set> parseSetsFromJson(JsonNode setsNode) {
        List<Set> setList = new ArrayList<>();

        if (setsNode == null || !setsNode.isArray()) {
            System.err.println("Error: Invalid or null sets data");
            return setList;
        }

        for (JsonNode setNode : setsNode) {
            Set set = new Set();
            set.setSetId(setNode.path("setID").asInt(-1));
            set.setNumber(setNode.path("number").asText("N/A"));
            set.setName(setNode.path("name").asText("N/A"));
            set.setYear(setNode.path("year").asText("N/A"));
            set.setTheme(setNode.path("theme").asText("N/A"));
            set.setSubtheme(setNode.path("subtheme").asText("N/A"));
            set.setPieces(setNode.path("pieces").asInt(-1));
            set.setMinifigs(setNode.path("minifigs").asInt(-1));
            set.setRating(setNode.path("rating").asDouble(-1.0));
            set.setReviewCount(setNode.path("reviewCount").asInt(-1));
            set.setImageUrl(setNode.path("image").path("imageURL").asText("N/A"));

            setList.add(set);
        }

        return setList;
    }
}