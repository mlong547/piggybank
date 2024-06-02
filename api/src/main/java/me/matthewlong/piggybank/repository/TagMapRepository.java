package me.matthewlong.piggybank.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapRepository extends R2dbcRepository<TagMapEntity, Integer> {
    
}
