package org.rebrickable.controller;

import org.rebrickable.BuildableSetResponse;
import org.rebrickable.RebrickablePartResponse;
import org.rebrickable.RebrickableResponse;
import org.rebrickable.service.RebrickableService;
import org.rebrickable.Set;
import org.rebrickable.service.UserCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rebrickable")
public class RebrickableController {

    private final UserCollectionService userCollectionService;

    private final RebrickableService rebrickableService;

    public RebrickableController(UserCollectionService userCollectionService, RebrickableService rebrickableService) {
        this.userCollectionService = userCollectionService;
        this.rebrickableService = rebrickableService;
    }

    @GetMapping("/sets/{setNum}")
    public Set getSetDetails(@PathVariable String setNum) {
        return rebrickableService.getSetDetails(setNum);
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
        return rebrickableService.searchSets(query, setNum, name, yearFrom, yearTo, page, pageSize);
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
    
        List<BuildableSetResponse> buildableSets = rebrickableService.getBuildableSets(
            username, query, setNum, name, yearFrom, yearTo, page, pageSize);
    
        return ResponseEntity.ok(buildableSets);
    }

    @PostMapping("/sets")
    public void saveSet(@RequestBody Set set) {
        rebrickableService.saveSet(set);
    }
}

