package org.rebrickable.controller;

import org.rebrickable.RebrickableResponse;
import org.rebrickable.service.RebrickableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RebrickableController {

    private final RebrickableService rebrickableService;

    @Autowired
    public RebrickableController(RebrickableService rebrickableService) {
        this.rebrickableService = rebrickableService;
    }

    @GetMapping("/lego/search")
    public RebrickableResponse searchLegoSets(
            @RequestParam("search") String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "ordering", required = false) String ordering
    ) {
        return rebrickableService.searchLegoSets(search, page, pageSize, ordering);
    }
}
