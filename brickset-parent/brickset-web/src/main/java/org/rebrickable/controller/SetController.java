package org.rebrickable.controller;

import org.rebrickable.RebrickableApiService;
import org.rebrickable.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        Map<String, String> params = new HashMap<>();
        params.put("search", query);
        return rebrickableApiService.getSets(params);
    }
}
