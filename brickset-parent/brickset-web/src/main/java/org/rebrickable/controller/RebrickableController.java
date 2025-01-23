package org.rebrickable.controller;

import org.rebrickable.RebrickablePartResponse;
import org.rebrickable.RebrickableResponse;
import org.rebrickable.service.RebrickableService;
import org.rebrickable.Set;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rebrickable")
public class RebrickableController {

    private final RebrickableService rebrickableService;

    public RebrickableController(RebrickableService rebrickableService) {
        this.rebrickableService = rebrickableService;
    }

    @GetMapping("/sets/search")
    public RebrickableResponse searchSets(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return rebrickableService.searchSets(query, page, pageSize);
    }

    @GetMapping("/sets/{setNum}/parts")
    public RebrickablePartResponse getSetParts(
            @PathVariable String setNum,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return rebrickableService.getSetParts(setNum, page, pageSize);
    }

    @PostMapping("/sets")
    public void saveSet(@RequestBody Set set) {
        rebrickableService.saveSet(set);
    }
}

