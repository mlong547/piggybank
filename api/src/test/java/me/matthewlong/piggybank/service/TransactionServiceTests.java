package me.matthewlong.piggybank.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.r2dbc.spi.R2dbcTimeoutException;
import me.matthewlong.piggybank.repository.TagEntity;
import me.matthewlong.piggybank.repository.TagMapRepository;
import me.matthewlong.piggybank.repository.TagRepository;
import me.matthewlong.piggybank.repository.TransactionEntity;
import me.matthewlong.piggybank.repository.TransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapRepository tagMapRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testCreateTransaction() {
        AtomicInteger entityID = new AtomicInteger();
        when(tagRepository.findOneByName(anyString())).thenAnswer(i -> {
            TagEntity t = new TagEntity();
            t.setId(entityID.incrementAndGet());
            t.setName(i.getArgument(0));
            return Mono.just(t);
        });
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> {
            TransactionEntity t = i.getArgument(0);
            t.setId(entityID.incrementAndGet());
            return Mono.just(t);
        });
        when(tagMapRepository.saveAll(anyIterable())).thenAnswer(i -> {
            return Flux.fromIterable(i.getArgument(0));
        });

        TransactionEntity t = new TransactionEntity();
        t.setAmount(100);
        t.setName("tickets");
        t.setTags(Arrays.asList(new String[]{"entertainment"}));
        t.setUserID("some user");
        StepVerifier.create(transactionService.save(t))
        .assertNext(transactionEntity -> {
            // assert that the returned entity has an id set
            Assertions.assertNotEquals(0, transactionEntity.getId());
        }).verifyComplete();
    }

    @Test
    void testCreateTransactionTagLookupError() {
        when(tagRepository.findOneByName(anyString())).thenThrow(new R2dbcTimeoutException("some timeout"));

        TransactionEntity t = new TransactionEntity();
        t.setAmount(100);
        t.setName("tickets");
        t.setTags(Arrays.asList(new String[]{"entertainment"}));
        t.setUserID("some user");
        StepVerifier.create(transactionService.save(t))
        .expectErrorMatches(throwable -> {
            return throwable instanceof R2dbcTimeoutException && throwable.getMessage().contains("some timeout");
        }).verify();
    }
}
