package org.rebrickable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Component
public class RebrickableApiClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl = "https://rebrickable.com/api/v3";
    private static final Duration MIN_REQUEST_INTERVAL = Duration.ofSeconds(1);
    private Instant lastApiCall = Instant.now();
    private static final Duration RATE_LIMIT = Duration.ofSeconds(1);

    public RebrickableApiClient(RestTemplate restTemplate, @Value("${rebrickable.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    private void respectRateLimit() {
        Instant now = Instant.now();
        long waitTime = RATE_LIMIT.minus(Duration.between(lastApiCall, now)).toMillis();
        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ignored) {}
        }
        lastApiCall = Instant.now();
    }

    private <T> T executeWithRetry(ThrowingSupplier<T> request) {
        while (true) {
            try {
                respectRateLimit();
                return request.get();
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    String retryAfter = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
                    if (retryAfter != null) {
                        try {
                            int seconds = Integer.parseInt(retryAfter);
                            try {
                                Thread.sleep(seconds * 1000L);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            continue;
                        } catch (NumberFormatException nfe) {
                            try {
                                Thread.sleep(MIN_REQUEST_INTERVAL.toMillis());
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            continue;
                        }
                    }
                }
                throw e;
            }
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get();
    }

    public Set getSetDetails(String setNum) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/lego/sets/" + setNum + "/")
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        return executeWithRetry(() -> restTemplate.getForObject(url, Set.class));
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

        return executeWithRetry(() -> {
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
        });
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/lego/sets/" + setNum + "/parts/")
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .queryParam("key", apiKey)
                .build()
                .toUriString();
        System.out.println("Requesting URL: " + url);
        return executeWithRetry(() -> restTemplate.getForObject(url, RebrickablePartResponse.class));
    }
}
