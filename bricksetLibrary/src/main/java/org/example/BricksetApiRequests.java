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

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /* TODO:
        java.net http implement :: DONE,
        Open feign,
        nationalize client review,
        spring feign

     */

    private String buildQuery(Map<String, String> params) throws IOException {
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
     } catch (InterruptedException e) {
         throw new RuntimeException(e);
     } catch (URISyntaxException e) {
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

