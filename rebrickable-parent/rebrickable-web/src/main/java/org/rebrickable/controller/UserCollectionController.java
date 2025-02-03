package org.rebrickable.controller;

import org.rebrickable.Set;
import org.rebrickable.SetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/collection")
public class UserCollectionController {

    private final SetRepository setRepository;

    public UserCollectionController(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @GetMapping
    public List<Set> getOwnedSets() {
        return setRepository.findByOwnedTrue();
    }

    @PutMapping("/{setNum}")
    public Set updateSetOwnership(@PathVariable String setNum, @RequestParam boolean owned) {
        Optional<Set> optionalSet = setRepository.findById(setNum);
        if (optionalSet.isPresent()) {
            Set set = optionalSet.get();
            set.setOwned(owned);
            return setRepository.save(set);
        } else {
            return null;
        }
    }
}
