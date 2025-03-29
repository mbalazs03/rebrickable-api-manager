package org.rebrickable.controller;

import org.rebrickable.Set;
import org.rebrickable.repository.SetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/collection")
public class UserCollectionController {

    private static final Logger logger = LoggerFactory.getLogger(UserCollectionController.class);

    private final SetRepository setRepository;

    @Autowired
    public UserCollectionController(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @GetMapping
    public List<Set> getOwnedSets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Fetching owned sets for user: {}", username);
        List<Set> sets = setRepository.findByOwner(username);
        logger.debug("Retrieved {} sets for user {}", sets.size(), username);
        return sets;
    }

    @PutMapping("/{setNum}")
    public Set updateSetOwnership(@PathVariable String setNum, @RequestParam boolean owned) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("User {} {} ownership of set {}", 
            username, owned ? "claiming" : "releasing", setNum);
        
        Optional<Set> optionalSet = setRepository.findById(setNum);
        if (optionalSet.isPresent()) {
            Set set = optionalSet.get();
            if (owned) {
                set.setOwner(username);
                logger.debug("Set {} ownership claimed by {}", setNum, username);
            } else {
                set.setOwner(null);
                logger.debug("Set {} ownership released by {}", setNum, username);
            }
            return setRepository.save(set);
        } else {
            logger.error("Failed to update ownership: Set {} not found", setNum);
            throw new RuntimeException("Set not found with setNum: " + setNum);
        }
    }
}
