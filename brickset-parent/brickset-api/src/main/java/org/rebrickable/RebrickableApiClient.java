package org.rebrickable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        String url = String.format("%s/lego/sets/?search=%s&page=%d&page_size=%d&key=%s", baseUrl, query, page, pageSize, apiKey);
        return restTemplate.getForObject(url, RebrickableResponse.class);
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        String url = String.format("%s/lego/sets/%s/parts/?page=%d&page_size=%d&key=%s", baseUrl, setNum, page, pageSize, apiKey);
        return restTemplate.getForObject(url, RebrickablePartResponse.class);
    }
}

