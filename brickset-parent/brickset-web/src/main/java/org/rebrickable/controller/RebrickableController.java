package org.rebrickable.controller;

import org.rebrickable.RebrickableApiService;
import org.rebrickable.RebrickableResponse;
import org.rebrickable.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rebrickable")
public class RebrickableController {

    private final RebrickableApiService rebrickableApiService;

    @Autowired
    public RebrickableController(RebrickableApiService rebrickableApiService) {
        this.rebrickableApiService = rebrickableApiService;
    }

    @GetMapping("/sets")
    public ResponseEntity<?> getSets(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(required = false, defaultValue = "name") String ordering) {
        try {
            Map<String, String> params = new HashMap<>();
            if (search != null && !search.isEmpty()) {
                params.put("search", search);
            }
            params.put("page", page.toString());
            params.put("page_size", pageSize.toString());
            params.put("ordering", ordering);

            RebrickableResponse response = rebrickableApiService.getRawResponse(params);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/sets/{setNum}")
    public ResponseEntity<?> getSetByNumber(@PathVariable int setNum) {
        try {
            RebrickableResponse response = rebrickableApiService.getRawResponseByNumber(setNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}