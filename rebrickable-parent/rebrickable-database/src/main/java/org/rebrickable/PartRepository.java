package org.rebrickable;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PartRepository extends MongoRepository<Part, String> {
    List<Part> findByNameContainingIgnoreCase(String name);
}
