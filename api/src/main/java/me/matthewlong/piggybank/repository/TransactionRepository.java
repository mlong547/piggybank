package me.matthewlong.piggybank.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends R2dbcRepository<TransactionEntity, Integer> {
  Flux<TransactionEntity> findAllByUserID(String userID);
}
