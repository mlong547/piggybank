package me.matthewlong.piggybank.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends R2dbcRepository<TagEntity, Integer> {
    
}
