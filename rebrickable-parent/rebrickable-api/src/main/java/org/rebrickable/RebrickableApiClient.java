package org.rebrickable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RebrickableApiClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl = "https://rebrickable.com/api/v3";

    public RebrickableApiClient(RestTemplate restTemplate, @Value("${rebrickable.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public RebrickableResponse searchSets(String query, int page, int pageSize) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/lego/sets/")
                .queryParam("search", query)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .queryParam("key", apiKey)
                .build()
                .toUriString();
        return restTemplate.getForObject(url, RebrickableResponse.class);
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/lego/sets/" + setNum + "/parts/")
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .queryParam("key", apiKey)
                .build()
                .toUriString();
        System.out.println("Requesting URL: " + url);
        return restTemplate.getForObject(url, RebrickablePartResponse.class);
    }
}
