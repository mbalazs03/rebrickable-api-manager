package org.rebrickable.controller;

import org.rebrickable.Set;
import org.rebrickable.repository.SetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MongoTestController {

    private final SetRepository setRepository;

    @Autowired
    public MongoTestController(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @GetMapping("/mongo/test")
    public List<Set> testMongoConnection() {
        Set sampleSet = new Set();
        sampleSet.setSetNum("12345");
        sampleSet.setName("Test Set");
        sampleSet.setYear(2025);
        sampleSet.setNumParts(100);
        sampleSet.setSetImgUrl("https://example.com/test.jpg");

        setRepository.save(sampleSet);

        return setRepository.findAll();
    }
}
