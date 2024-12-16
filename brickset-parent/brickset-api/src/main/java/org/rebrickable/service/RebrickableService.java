package org.rebrickable.service;

import org.rebrickable.RebrickableClient;
import org.rebrickable.RebrickableResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RebrickableService {

    private final RebrickableClient rebrickableClient;

    @Value("${rebrickable.api.key}")
    private String apiKey;

    public RebrickableService(RebrickableClient rebrickableClient) {
        this.rebrickableClient = rebrickableClient;
    }

    public RebrickableResponse searchLegoSets(String searchTerm, Integer page, Integer pageSize, String ordering) {
        return rebrickableClient.searchSets(searchTerm, page, pageSize, ordering, apiKey);
    }
}
