package org.rebrickable.controller;

import org.rebrickable.BuildableSetResponse;
import org.rebrickable.RebrickablePartResponse;
import org.rebrickable.RebrickableResponse;
import org.rebrickable.service.RebrickableService;
import org.rebrickable.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rebrickable")
public class RebrickableController {

    private static final Logger logger = LoggerFactory.getLogger(RebrickableController.class);

    private final RebrickableService rebrickableService;

    public RebrickableController(RebrickableService rebrickableService) {
        this.rebrickableService = rebrickableService;
    }

    @GetMapping("/sets/{setNum}")
    public Set getSetDetails(@PathVariable String setNum) {
        logger.info("Fetching details for set: {}", setNum);
        Set set = rebrickableService.getSetDetails(setNum);
        logger.debug("Retrieved set details for {}: {}", setNum, set);
        return set;
    }

    @GetMapping("/sets/search")
    public RebrickableResponse searchSets(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String setNum,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize) {
        logger.info("Searching sets with params - query: {}, setNum: {}, name: {}, yearFrom: {}, yearTo: {}, page: {}, pageSize: {}",
                query, setNum, name, yearFrom, yearTo, page, pageSize);
        RebrickableResponse response = rebrickableService.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
        logger.debug("Search returned {} results", response.getCount());
        return response;
    }

    @GetMapping("/sets/{setNum}/parts")
    public RebrickablePartResponse getSetParts(
            @PathVariable String setNum,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return rebrickableService.getSetParts(setNum, page, pageSize);
    }

    @GetMapping("/sets/buildable")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBuildableSets(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String setNum,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
    
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("User {} requesting buildable sets", username);
    
        List<BuildableSetResponse> buildableSets = rebrickableService.getBuildableSets(
            username, query, setNum, name, yearFrom, yearTo, page, pageSize);
    
        logger.debug("Found {} buildable sets for user {}", buildableSets.size(), username);
        return ResponseEntity.ok(buildableSets);
    }

    @PostMapping("/sets")
    public void saveSet(@RequestBody Set set) {
        rebrickableService.saveSet(set);
    }
}

