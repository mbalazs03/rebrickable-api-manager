package org.rebrickable.service;

import org.rebrickable.*;
import org.rebrickable.Set;
import org.rebrickable.dto.SetPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RebrickableService {

    private static final Logger logger = LoggerFactory.getLogger(RebrickableService.class);

    private final RebrickableApiClient apiClient;
    private final SetRepository setRepository;
    private final Map<String, List<SetPart>> partsCache = new ConcurrentHashMap<>();

    public RebrickableService(RebrickableApiClient apiClient, SetRepository setRepository) {
        this.apiClient = apiClient;
        this.setRepository = setRepository;
        logger.info("RebrickableService initialized");
    }

    public Set getSetDetails(String setNum) {
        logger.info("Fetching details for set: {}", setNum);
        Set set = apiClient.getSetDetails(setNum);
        logger.debug("Fetched set details: {}", set);
        return set;
    }

    public RebrickableResponse searchSets(String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        logger.info("Searching sets with parameters: query={}, setNum={}, name={}, yearFrom={}, yearTo={}, page={}, pageSize={}",
                query, setNum, name, yearFrom, yearTo, page, pageSize);
        RebrickableResponse response = apiClient.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
        logger.debug("Number of sets found: {}", response != null ? response.getCount() : "null");
        return response;
    }

    public RebrickablePartResponse getSetParts(String setNum, int page, int pageSize) {
        logger.info("Fetching parts for set: {} (page={}, pageSize={})", setNum, page, pageSize);
        RebrickablePartResponse response = apiClient.getSetParts(setNum, page, pageSize);
        logger.debug("Number of parts fetched: {}", response != null ? response.getCount() : "null");
        return response;
    }

    public void saveSet(Set set) {
        logger.info("Saving set: {}", set);
        setRepository.save(set);
        logger.debug("Set saved successfully");
    }

    private List<SetPart> getCachedSetParts(String setNum) {
        logger.info("Checking cache for set: {}", setNum);
        List<SetPart> cachedParts = partsCache.computeIfAbsent(setNum, k -> {
            logger.info("Cache miss for set: {}. Calling API...", k);
            RebrickablePartResponse response = apiClient.getSetParts(k, 1, 1000);
            logger.debug("API returned {} parts", response.getResults().size());
            return response.getResults();
        });
        return cachedParts;
    }

    public List<BuildableSetResponse> getBuildableSets(String username, String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        logger.info("Fetching buildable sets for user: {}", username);
        List<Set> userSets = setRepository.findByOwner(username);
        logger.debug("User {} owns {} sets", username, userSets.size());
        Map<String, Integer> userPartsInventory = new HashMap<>();

        for (Set ownedSet : userSets) {
            List<SetPart> parts = getCachedSetParts(ownedSet.getSetNum());
            for (SetPart part : parts) {
                String partNum = part.getPart().getPartNum();
                userPartsInventory.merge(partNum, part.getQuantity(), Integer::sum);
            }
        }

        logger.debug("User parts inventory: {}", userPartsInventory);

        RebrickableResponse searchResults = apiClient.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
        logger.info("Found {} candidate sets", searchResults.getResults().size());
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
            logger.debug("Candidate set {} has completion percentage: {}", candidateSet.getSetNum(), completionPercentage);
        }

        response.sort(Comparator.comparingDouble(BuildableSetResponse::getCompletionPercentage).reversed());
        logger.info("Total buildable sets returned: {}", response.size());
        return response;
    }
}
