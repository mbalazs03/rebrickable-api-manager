package org.rebrickable.controller;

import org.rebrickable.BuildableSetResponse;
import org.rebrickable.Set;
import org.rebrickable.SetRepository;
import org.rebrickable.service.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/collection")
public class UserCollectionController {

    private final SetRepository setRepository;
    private final UserCollectionService userCollectionService;

    @Autowired
    public UserCollectionController(SetRepository setRepository, UserCollectionService userCollectionService) {
        this.setRepository = setRepository;
        this.userCollectionService = userCollectionService;
    }

    @GetMapping
    public List<Set> getOwnedSets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return setRepository.findByOwner(username);
    }

    @GetMapping("/buildable")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBuildableSets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String username = authentication.getName();
            List<BuildableSetResponse> buildableSets = userCollectionService.getBuildableSets(username, null, null, null, null, null, page, pageSize);
            return ResponseEntity.ok(buildableSets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving buildable sets: " + e.getMessage());
        }
    }

    @PutMapping("/{setNum}")
    public Set updateSetOwnership(@PathVariable String setNum, @RequestParam boolean owned) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Set> optionalSet = setRepository.findById(setNum);
        if (optionalSet.isPresent()) {
            Set set = optionalSet.get();
            if (owned) {
                set.setOwner(username);
            } else {
                set.setOwner(null);
            }
            return setRepository.save(set);
        } else {
            throw new RuntimeException("Set not found with setNum: " + setNum);
        }
    }
}
