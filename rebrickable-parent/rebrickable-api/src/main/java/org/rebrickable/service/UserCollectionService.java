package org.rebrickable.service;

import org.rebrickable.*;
import org.rebrickable.dto.SetPart;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserCollectionService {

    private final SetRepository setRepository;
    private final RebrickableService rebrickableService;
    private final UserRepository userRepository;
    private final Map<String, List<SetPart>> setPartsCache = new HashMap<>();

    public UserCollectionService(SetRepository setRepository, RebrickableService rebrickableService, UserRepository userRepository) {
        this.setRepository = setRepository;
        this.rebrickableService = rebrickableService;
        this.userRepository = userRepository;
    }

    private List<SetPart> getSetPartsWithCache(String setNum) {
        return setPartsCache.computeIfAbsent(setNum, k -> {
            RebrickablePartResponse response = rebrickableService.getSetParts(k, 1, 1000);
            return response != null ? response.getResults() : Collections.emptyList();
        });
    }

    public List<BuildableSetResponse> getBuildableSets(String username, int page, int pageSize) {
        List<org.rebrickable.Set> userSets = setRepository.findByOwner(username);
        Map<String, Integer> userParts = new HashMap<>();

        for (org.rebrickable.Set userSet : userSets) {
            List<SetPart> parts = getSetPartsWithCache(userSet.getSetNum());
            for (SetPart part : parts) {
                String partNum = part.getPart().getPartNum();
                userParts.put(partNum, userParts.getOrDefault(partNum, 0) + part.getQuantity());
            }
        }

        RebrickableResponse availableSetsResponse = rebrickableService.searchSets("", null, null, null, null, 1, 20);
        List<BuildableSetResponse> buildableSets = new ArrayList<>();

        for (org.rebrickable.Set availableSet : availableSetsResponse.getResults()) {
            List<SetPart> setParts = getSetPartsWithCache(availableSet.getSetNum());
            int totalParts = setParts.size();
            int matchingParts = 0;
            List<String> missingParts = new ArrayList<>();

            for (SetPart part : setParts) {
                String partNum = part.getPart().getPartNum();
                int userQuantity = userParts.getOrDefault(partNum, 0);
                if (userQuantity >= part.getQuantity()) {
                    matchingParts++;
                } else {
                    missingParts.add(String.format("%s (%d db hiányzik)", 
                        part.getPart().getName(), 
                        part.getQuantity() - userQuantity));
                }
            }

            double completionPercentage = (double) matchingParts / totalParts * 100;

            if (completionPercentage > 0) {
                buildableSets.add(new BuildableSetResponse(availableSet, completionPercentage, missingParts));
            }
        }

        buildableSets.sort((a, b) -> Double.compare(b.getCompletionPercentage(), a.getCompletionPercentage()));

        return buildableSets.stream()
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}
