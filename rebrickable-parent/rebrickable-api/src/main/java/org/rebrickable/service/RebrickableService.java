package org.rebrickable.service;

import org.rebrickable.*;
import org.springframework.stereotype.Service;

@Service
public class RebrickableService {

    private final RebrickableApiClient apiClient;
    private final SetRepository setRepository;

    public RebrickableService(RebrickableApiClient apiClient, SetRepository setRepository) {
        this.apiClient = apiClient;
        this.setRepository = setRepository;
    }

    public RebrickableResponse searchSets(String query, int page, int pageSize) {
        return apiClient.searchSets(query, page, pageSize);
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        return apiClient.getSetParts(setNum, page, pageSize);
    }

    public void saveSet(Set set) {
        setRepository.save(set);
    }
}

