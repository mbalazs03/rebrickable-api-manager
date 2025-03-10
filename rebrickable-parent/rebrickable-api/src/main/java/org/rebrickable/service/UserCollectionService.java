package org.rebrickable.service;

import org.rebrickable.*;
import org.rebrickable.Set;
import org.rebrickable.dto.SetPart;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserCollectionService {

    private final SetRepository setRepository;
    private final RebrickableService rebrickableService;
    private final Map<String, List<SetPart>> setPartsCache = new ConcurrentHashMap<>();

    public UserCollectionService(SetRepository setRepository, RebrickableService rebrickableService) {
        this.setRepository = setRepository;
        this.rebrickableService = rebrickableService;
    }

    private List<SetPart> getSetPartsWithCache(String setNum) {
        return setPartsCache.computeIfAbsent(setNum, k -> {
            RebrickablePartResponse response = rebrickableService.getSetParts(k, 1, 1000);
            return response != null ? response.getResults() : Collections.emptyList();
        });
    }

    public List<BuildableSetResponse> getBuildableSets(String username, String query, String setNum, String name, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        List<Set> userSets = setRepository.findByOwner(username);
        Map<String, Integer> userPartsInventory = new HashMap<>();

        for (Set ownedSet : userSets) {
            List<SetPart> parts = getSetPartsWithCache(ownedSet.getSetNum());
            for (SetPart part : parts) {
                String partNum = part.getPart().getPartNum();
                userPartsInventory.merge(partNum, part.getQuantity(), Integer::sum);
            }
        }

        RebrickableResponse searchResults = rebrickableService.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
        List<BuildableSetResponse> response = new ArrayList<>();

        for (Set candidateSet : searchResults.getResults()) {
            List<SetPart> requiredParts = getSetPartsWithCache(candidateSet.getSetNum());

            int totalParts = requiredParts.stream().mapToInt(SetPart::getQuantity).sum();
            int matchedParts = 0;
            List<String> missingParts = new ArrayList<>();

            for (SetPart requiredPart : requiredParts) {
                int ownedQty = userPartsInventory.getOrDefault(requiredPart.getPart().getPartNum(), 0);
                matchedParts += Math.min(ownedQty, requiredPart.getQuantity());

                if (ownedQty < requiredPart.getQuantity()) {
                    missingParts.add(requiredPart.getPart().getName() + " (" + (requiredPart.getQuantity() - ownedQty) + " missing)");
                }
            }

            double completionPercentage = totalParts == 0 ? 0 : ((double) matchedParts / totalParts) * 100.0;
            response.add(new BuildableSetResponse(candidateSet, completionPercentage, missingParts));
        }

        response.sort(Comparator.comparingDouble(BuildableSetResponse::getCompletionPercentage).reversed());

        return response;
    }
}
