package org.rebrickable;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SetRepository extends MongoRepository<Set, String> {
    List<Set> findByNameContainingIgnoreCase(String name);

    List<Set> findByOwner(String owner);
}
