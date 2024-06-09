package me.matthewlong.piggybank.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface TagRepository extends R2dbcRepository<TagEntity, Integer> {
    Mono<TagEntity> findOneByName(String name);
}
