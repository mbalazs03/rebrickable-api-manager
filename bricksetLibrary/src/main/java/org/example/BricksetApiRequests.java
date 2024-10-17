package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import io.github.cdimascio.dotenv.Dotenv;
public class BricksetApiRequests {
    Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private  final String API_URL = dotenv.get("API_URL");
    private final String API_KEY = dotenv.get("API_KEY");
    private  final String USER_HASH = dotenv.get("USER_HASH");



    //java.net http, Open feign, nationalize client, spring feign

    private String buildQuery(Map<String, String> params) throws IOException {
        String query = String.format("apiKey=%s&userHash=%s&params=",
                URLEncoder.encode(API_KEY, StandardCharsets.UTF_8),
                URLEncoder.encode(USER_HASH, StandardCharsets.UTF_8));

        String paramString = params.entrySet()
                .stream()
                .map(entry -> String.format("'%s':'%s'", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));

        return query + "{" + paramString + "}";
    }

    private String sendRequest(String query) {
        try {
            URL url = new URL(API_URL + "?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
               while ((inputLine = in.readLine()) != null) {
                   content.append(inputLine).append("\n");
                }
                in.close();
                connection.disconnect();
                return content.toString();
            } else {
                return "Error: Server returned HTTP response code: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }

    // Main method that accepts a dynamic map of parameters
    public String getSet(Map<String, String> params) {
        try {
            String query = buildQuery(params);
            return sendRequest(query);
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }

    // Convenience method: get set by year only
    public String getSet(int year) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        return getSet(params);
    }

    // Convenience method: get set by year and theme
    public String getSet(int year, String theme) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        return getSet(params);
    }

    // Convenience method: get set by year, theme, and name
    public String getSet(int year, String theme, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        params.put("name", name);
        return getSet(params);
    }

    // Convenience method: get set by year, theme, name, and category
    public String getSet(int year, String theme, String name, String category) {
        Map<String, String> params = new HashMap<>();
        params.put("year", String.valueOf(year));
        params.put("theme", theme);
        params.put("name", name);
        params.put("category", category);
        return getSet(params);
    }

}

