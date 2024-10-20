package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.Feign;
import feign.FeignException;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.cdimascio.dotenv.Dotenv;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BricksetApiRequests {

    Dotenv dotenv = Dotenv.configure().load();

    private final String API_URL = dotenv.get("API_URL");
    private final String API_KEY = dotenv.get("API_KEY");
    private final String USER_HASH = dotenv.get("USER_HASH");

    private final BricksetClient bricksetClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(BricksetClient.class, API_URL);

    /* TODO:
        java.net http implement :: DONE,
        output --> json file :: DONE
        Open feign :: WAITLIST
        nationalize client review :: WAITLIST
        spring feign :: wAITLIST
     */

    private String buildQuery(Map<String, String> params) throws IOException {
        String paramString = params.entrySet()
                .stream()
                .map(entry -> String.format("'%s':'%s'", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));
        String jsonParams = "{" + paramString + "}";
        return URLEncoder.encode(jsonParams, StandardCharsets.UTF_8);
    }

    public String extractKeyInfo(String jsonResponse) {

        StringBuilder keyInfo = new StringBuilder();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.get("status").asText().equals("success")) {
                JsonNode setsArray = rootNode.get("sets");

                for (JsonNode setInfo : setsArray) {
                    String name = setInfo.has("name") ? setInfo.path("name").asText() : "Unknown Set";
                    String theme = setInfo.has("theme") ? setInfo.path("theme").asText() : "Unknown Theme";
                    int year = setInfo.has("year") ? setInfo.path("year").asInt() : -1;
                    int pieces = setInfo.has("pieces") ? setInfo.path("pieces").asInt() : -1;
                    int minifigs = setInfo.has("minifigs") ? setInfo.path("minifigs").asInt() : -1;
                    double rating = setInfo.has("rating") ? setInfo.path("rating").asDouble() : -1.0;
                    int reviewCount = setInfo.has("reviewCount") ? setInfo.path("reviewCount").asInt() : -1;

                    keyInfo.append("Set Name: ").append(name)
                            .append("\nTheme: ").append(theme)
                            .append("\nYear: ").append(year != -1 ? year : "Unknown Year")
                            .append("\nPieces: ").append(pieces != -1 ? pieces : "Unknown Pieces")
                            .append("\nMinifigures: ").append(minifigs != -1 ? minifigs : "Unknown Minifigures")
                            .append("\nRating: ").append(rating != -1.0 ? rating : "Unknown Rating")
                            .append("\nReview Count: ").append(reviewCount != -1 ? reviewCount : "Unknown Review Count")
                            .append("\n-------------------------\n");

                }
                return keyInfo.toString();
            } else {
                return "No data found";
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void extarctKeyInfoToFile(String jsonResponse, String outputFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode setsArrayNode = objectMapper.createArrayNode();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.path("status").asText().equals("success")) {
                JsonNode setsArray = rootNode.path("sets");

                for (JsonNode setInfo : setsArray) {
                    ObjectNode setNode = objectMapper.createObjectNode();

                    setNode.put("name", setInfo.has("name") ? setInfo.path("name").asText() : "Unknown Set");
                    setNode.put("theme", setInfo.has("theme") ? setInfo.path("theme").asText() : "Unknown Theme");
                    setNode.put("year", setInfo.has("year") ? setInfo.path("year").asInt() : -1);
                    setNode.put("pieces", setInfo.has("pieces") ? setInfo.path("pieces").asInt() : -1);
                    setNode.put("minifigs", setInfo.has("minifigs") ? setInfo.path("minifigs").asInt() : -1);
                    setNode.put("rating", setInfo.has("rating") ? setInfo.path("rating").asDouble() : -1.0);
                    setNode.put("reviewCount", setInfo.has("reviewCount") ? setInfo.path("reviewCount").asInt() : -1);

                    setsArrayNode.add(setNode);
                }

                try (FileWriter fileWriter = new FileWriter(outputFilePath)) {

                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, setsArrayNode);
                    System.out.println("Data successfully written to " + outputFilePath);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("No data found");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public String getSet(Map<String, String> params) {
        try {
            String query = buildQuery(params);
            System.out.println("Constructed Query: " + query);  // Print the query for debugging
            return getRequest(query);
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }

    public String getRequest(String query) {
        System.out.println("Sending request with parameters: " + query);

        try {
            BricksetResponse response = bricksetClient.getSets(API_KEY, USER_HASH, query);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            System.out.println("Raw API Response: " + jsonResponse);
            return jsonResponse;
        } catch (FeignException e) {
            System.err.println("Error response from server: " + e.contentUTF8());
            return "Error response from server: " + e.contentUTF8();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }
}

