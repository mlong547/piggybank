package me.matthewlong.piggybank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.matthewlong.piggybank.repository.TagEntity;
import me.matthewlong.piggybank.repository.TagMapEntity;
import me.matthewlong.piggybank.repository.TagMapRepository;
import me.matthewlong.piggybank.repository.TagRepository;
import me.matthewlong.piggybank.repository.TransactionEntity;
import me.matthewlong.piggybank.repository.TransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionService {
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private TagMapRepository tagMapRepository;

    public Mono<TransactionEntity> findById(Integer id) {
        return this.transactionRepository.findById(id)
        .flatMap(transactionEntity -> {
            return this.tagMapRepository.findAllByTransactionId(transactionEntity.getId())
            .map(tagMapEntity -> tagMapEntity.getTagId())
            .collectList()
            .flatMapMany(this.tagRepository::findAllById)
            .map(tagEntity -> tagEntity.getName())
            .collectList()
            .map(tags -> {
                transactionEntity.setTags(tags);
                return transactionEntity;
            });
        });
    }

    public Flux<TransactionEntity> findAllByUserID(String userID) {
        return this.transactionRepository.findAllByUserID(userID)
        .flatMap(transactionEntity -> {
            return this.tagMapRepository.findAllByTransactionId(transactionEntity.getId())
            .map(tagMapEntity -> tagMapEntity.getTagId())
            .collectList()
            .flatMapMany(this.tagRepository::findAllById)
            .map(tagEntity -> tagEntity.getName())
            .collectList()
            .map(tags -> {
                transactionEntity.setTags(tags);
                return transactionEntity;
            });
        });
    }

    @Transactional
    public Mono<TransactionEntity> save(TransactionEntity transactionEntity) {
        // create tag entities, then save the transaction entity, then save the tag maps
        return Flux.fromIterable(transactionEntity.getTags())
        .map(tagName -> {
            TagEntity t = new TagEntity();
            t.setName(tagName);
            return t;
        })
        .flatMap(tagEntity -> {
            return this.tagRepository.findOneByName(tagEntity.getName())
            .switchIfEmpty(Mono.defer(() -> this.tagRepository.save(tagEntity)));
        })
        .collectList()
        .flatMap(tagEntities -> {
            return this.transactionRepository.save(transactionEntity)
            .map(savedTransaction -> {
                return new Tuple<TransactionEntity, List<TagEntity>>(savedTransaction, tagEntities);
            });
        })
        .flatMap(tuple -> {
            List<TagMapEntity> tagMaps = new ArrayList<>();
            for (TagEntity t : tuple.right) {
                TagMapEntity tagMapEntity = new TagMapEntity();
                tagMapEntity.setTagId(t.getId());
                tagMapEntity.setTransactionId(tuple.left.getId());
                tagMaps.add(tagMapEntity);
            }
            return this.tagMapRepository.saveAll(tagMaps)
            .collectList()
            .map(_unused -> {
                return tuple.left;
            });
        });
    }

    @Transactional
    public Mono<Void> delete(Integer transactionID) {
        // Delete the tagmaps, then delete the transaction
        return this.tagMapRepository.findAllByTransactionId(transactionID)
        .flatMap(tagMapEntity -> this.tagMapRepository.deleteById(tagMapEntity.getId()))
        .then(this.transactionRepository.deleteById(transactionID));
    }

    public class Tuple<T, U> {
        T left;
        U right;

        public Tuple(T t, U u) {
            this.left = t;
            this.right = u;
        }
    }
}
