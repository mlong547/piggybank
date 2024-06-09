package me.matthewlong.piggybank.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TagMapRepository extends R2dbcRepository<TagMapEntity, Integer> {
    Flux<TagMapEntity> findAllByTransactionId(Integer transactionId);
    Mono<TagMapEntity> findOneByTagId(Integer tagId);
}
