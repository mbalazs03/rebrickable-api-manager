package org.rebrickable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;

public class RebrickableApiRequests {

    private final Dotenv dotenv = Dotenv.configure().load();
    private final String API_URL = "https://rebrickable.com/api/v3";
    private final String API_KEY;

    private final RebrickableClient rebrickableClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RebrickableApiRequests() {
        API_KEY = dotenv.get("REBRICKABLE_API_KEY");
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is missing. Please set REBRICKABLE_API_KEY in your environment.");
        }

        rebrickableClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(RebrickableClient.class, API_URL);
    }

    public RebrickableResponse getSetByNumber(int set_num) {
        try {
            if (set_num == 0) {
                throw new IllegalArgumentException("Set number cannot be null or empty.");
            }
            return rebrickableClient.getSetByNumber(set_num, API_KEY);
        } catch (Exception e) {
            System.err.println("Error fetching set by number: " + e.getMessage());
            throw new RuntimeException("Failed to fetch set by number.", e);
        }
    }

    public RebrickableResponse searchSets(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                throw new IllegalArgumentException("Search query cannot be null or empty.");
            }
            return rebrickableClient.searchSets(query, API_KEY);
        } catch (Exception e) {
            System.err.println("Error searching sets: " + e.getMessage());
            throw new RuntimeException("Failed to search sets.", e);
        }
    }

    public RebrickableResponse getAllSets() {
        try {
            return rebrickableClient.getAllSets(API_KEY);
        } catch (Exception e) {
            System.err.println("Error fetching all sets: " + e.getMessage());
            throw new RuntimeException("Failed to fetch all sets.", e);
        }
    }

    public void extractAndSaveSetsToFile(String jsonResponse, String filePath) {
        try {
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                throw new IllegalArgumentException("JSON response cannot be null or empty.");
            }
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new IllegalArgumentException("File path cannot be null or empty.");
            }

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            if (resultsNode.isMissingNode()) {
                throw new IllegalArgumentException("Invalid JSON: Missing 'results' node.");
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), resultsNode);
            System.out.println("Sets successfully saved to file: " + filePath);
        } catch (Exception e) {
            System.err.println("Error extracting sets to file: " + e.getMessage());
            throw new RuntimeException("Failed to extract and save sets to file.", e);
        }
    }
}
