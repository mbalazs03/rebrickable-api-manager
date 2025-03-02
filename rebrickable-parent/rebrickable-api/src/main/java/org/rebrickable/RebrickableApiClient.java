package org.rebrickable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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

    public RebrickableResponse searchSets(String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/lego/sets/")
                .queryParam("key", apiKey)
                .queryParam("page", page)
                .queryParam("page_size", pageSize);

        if (query != null && !query.isEmpty()) {
            builder.queryParam("search", query);
        }
        if (setNum != null && !setNum.isEmpty()) {
            builder.queryParam("set_num", setNum);
        }
        if (name != null && !name.isEmpty()) {
            builder.queryParam("name", name);
        }
        if (yearFrom != null) {
            builder.queryParam("min_year", yearFrom);
        }
        if (yearTo != null) {
            builder.queryParam("max_year", yearTo);
        }

        String url = builder.build().toUriString();
        System.out.println("Requesting URL: " + url);

        try {
            RebrickableResponse response = restTemplate.getForObject(url, RebrickableResponse.class);
            if (response != null) {
                if (response.getNext() != null) {
                    response.setNext(response.getNext().replace(baseUrl + "/lego/sets/", "/api/rebrickable/sets/search"));
                }
                if (response.getPrevious() != null) {
                    response.setPrevious(response.getPrevious().replace(baseUrl + "/lego/sets/", "/api/rebrickable/sets/search"));
                }
            }
            return response;
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getMessage());
            return new RebrickableResponse();
        }
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
