package org.rebrickable.controller;

import org.rebrickable.Set;
import org.rebrickable.SetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/collection")
public class UserCollectionController {

    private final SetRepository setRepository;

    @Autowired
    public UserCollectionController(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @GetMapping
    public List<Set> getOwnedSets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return setRepository.findByOwner(username);
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
