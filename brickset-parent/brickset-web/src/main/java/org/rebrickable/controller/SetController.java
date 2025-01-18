package org.rebrickable.controller;

import org.rebrickable.RebrickableApiService;
import org.rebrickable.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sets")
public class SetController {

    private final RebrickableApiService rebrickableApiService;

    @Autowired
    public SetController(RebrickableApiService rebrickableApiService) {
        this.rebrickableApiService = rebrickableApiService;
    }

    @GetMapping("/search")
    public List<Set> searchSets(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query parameter cannot be null or empty.");
        }
        return rebrickableApiService.getSets(Map.of("search", query));
    }

    @PostMapping("/save")
    public void saveSet(@RequestBody Set set) {
        rebrickableApiService.saveSet(set);
    }
}
