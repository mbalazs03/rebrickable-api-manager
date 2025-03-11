package org.rebrickable.service;

import org.rebrickable.*;
import org.rebrickable.Set;
import org.rebrickable.dto.SetPart;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RebrickableService {

    private final RebrickableApiClient apiClient;
    private final SetRepository setRepository;
    private final Map<String, List<SetPart>>  partsCache = new ConcurrentHashMap<>();


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

    private List<SetPart> getCachedSetParts(String setNum) {
        return partsCache.computeIfAbsent(setNum, k -> apiClient.getSetParts(k, 1, 1000).getResults());
    }

    public List<BuildableSetResponse> getBuildableSets(String username, String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        List<Set> userSets = setRepository.findByOwner(username);
        Map<String, Integer> userPartsInventory = new HashMap<>();

        for (Set ownedSet : userSets) {
            List<SetPart> parts = getCachedSetParts(ownedSet.getSetNum());
            for (SetPart part : parts) {
                String partNum = part.getPart().getPartNum();
                userPartsInventory.merge(partNum, part.getQuantity(), Integer::sum);
            }
        }

        RebrickableResponse searchResults = apiClient.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
        List<BuildableSetResponse> response = new ArrayList<>();

        for (Set candidateSet : searchResults.getResults()) {
            List<SetPart> requiredParts = getCachedSetParts(candidateSet.getSetNum());

            int totalPartsRequired = requiredParts.stream().mapToInt(SetPart::getQuantity).sum();
            int matchedPartsCount = 0;
            List<String> missingParts = new ArrayList<>();

            for (SetPart requiredPart : requiredParts) {
                String partNum = requiredPart.getPart().getPartNum();
                int ownedQty = userPartsInventory.getOrDefault(partNum, 0);
                matchedPartsCount += Math.min(ownedQty, requiredPart.getQuantity());

                if (ownedQty < requiredPart.getQuantity()) {
                    missingParts.add(String.format("%s (%d missing)", requiredPart.getPart().getName(), requiredPart.getQuantity() - ownedQty));
                }
            }

            double completionPercentage = totalPartsRequired == 0
                ? 0
                : (matchedPartsCount / (double) totalPartsRequired) * 100;

            response.add(new BuildableSetResponse(candidateSet, completionPercentage, missingParts));
        }

        response.sort(Comparator.comparingDouble(BuildableSetResponse::getCompletionPercentage).reversed());

        return response;
    }
    
}
