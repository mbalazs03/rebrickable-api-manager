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

    public Set getSetDetails(String setNum) {
        return apiClient.getSetDetails(setNum);
    }

    public RebrickableResponse searchSets(String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        return apiClient.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        return apiClient.getSetParts(setNum, page, pageSize);
    }

    public void saveSet(Set set) {
        setRepository.save(set);
    }
}

