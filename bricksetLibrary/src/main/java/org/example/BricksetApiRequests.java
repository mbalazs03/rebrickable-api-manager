package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.cdimascio.dotenv.Dotenv;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BricksetApiRequests {

    Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private  final String API_URL = dotenv.get("API_URL");
    private final String API_KEY = dotenv.get("API_KEY");
    private  final String USER_HASH = dotenv.get("USER_HASH");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /* TODO:
        java.net http implement :: DONE,
        output --> json file :: STARTED
        Open feign :: WAITLIST
        nationalize client review :: WAITLIST
        spring feign :: wAITLIST
     */

    private String buildQuery(Map<String, String> params) throws IOException {
        assert USER_HASH != null;
        assert API_KEY != null;
        String query = String.format("apiKey=%s&userHash=%s",
                URLEncoder.encode(API_KEY, StandardCharsets.UTF_8),
                URLEncoder.encode(USER_HASH, StandardCharsets.UTF_8));

        String paramString = params.entrySet()
                .stream()
                .map(entry -> String.format("'%s':'%s'", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));

        String encodedParams = URLEncoder.encode("{" + paramString + "}", StandardCharsets.UTF_8);
        return query + "&params=" + encodedParams;
    }

    private String sendRequest(String query) {
     try {
         URI uri = new URI(API_URL + "?" + query);
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(uri)
                 .GET()
                 .build();

         HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

         if (response.statusCode() == 200) {
             return response.body();
         } else {
             return "Error: Server returned HTTP response code: " + response.statusCode();
         }
     } catch (IOException e) {
         e.printStackTrace();
         return "Exception occurred: " + e.getMessage();
     } catch (InterruptedException | URISyntaxException e) {
         throw new RuntimeException(e);
     }
    }

    public String getSet(Map<String, String> params) {
        try {
            String query = buildQuery(params);
            return sendRequest(query);
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }

    public String extractKeyInfoToFile(String jsonResponse) {

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

    public String getSet(int year) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        return getSet(params);
    }

    public String getSet(int year, String theme) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        return getSet(params);
    }

    public String getSet(int year, String theme, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        params.put("name", name);
        return getSet(params);
    }

    public String getSet(int year, String theme, String name, String category) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        params.put("name", name);
        params.put("category", category);
        return getSet(params);
    }

}

