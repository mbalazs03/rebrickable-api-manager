package org.brickset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class BricksetApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Set> parseSetsFromJson(String jsonResponse) {
        List<Set> setList = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.path("status").asText().equals("success")) {
                JsonNode setsArray = rootNode.path("sets");

                for (JsonNode setNode : setsArray) {
                    Set set = new Set();
                    set.setSetId(setNode.path("setId").asInt(-1));
                    set.setName(setNode.path("name").asText("Unknown Set"));
                    set.setYear(setNode.path("year").asText("Unknown Year"));
                    set.setTheme(setNode.path("theme").asText("Unknown Theme"));
                    set.setPieces(setNode.path("pieces").asInt(-1));
                    set.setMinifigs(setNode.path("minifigs").asInt(-1));
                    set.setRating(setNode.path("rating").asDouble(-1.0));
                    set.setReviewCount(setNode.path("reviewCount").asInt(-1));

                    setList.add(set);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing JSON response: " + e.getMessage());
        }

        return setList;
    }
}
