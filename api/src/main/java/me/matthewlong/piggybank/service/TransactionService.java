package me.matthewlong.piggybank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.matthewlong.piggybank.repository.TransactionEntity;
import me.matthewlong.piggybank.repository.TransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionService {
    @Autowired private TransactionRepository transactionRepository;

    public Mono<TransactionEntity> findById(Integer id) {
        return this.transactionRepository.findById(id);
    }

    public Flux<TransactionEntity> findAllByUserID(String userID) {
        return this.transactionRepository.findAllByUserID(userID);
    }

    public Mono<TransactionEntity> save(TransactionEntity transactionEntity) {
        return this.transactionRepository.save(transactionEntity);
    }
}
