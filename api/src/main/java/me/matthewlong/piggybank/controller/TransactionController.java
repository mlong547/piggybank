package me.matthewlong.piggybank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import me.matthewlong.piggybank.api.ErrorResponse;
import me.matthewlong.piggybank.api.TransactionRequest;
import me.matthewlong.piggybank.api.TransactionResponse;
import me.matthewlong.piggybank.repository.TransactionEntity;
import me.matthewlong.piggybank.repository.TransactionRepository;
import reactor.core.publisher.Mono;

@Component
public class TransactionController {
    @Autowired private Validator validator;
    @Autowired private TransactionRepository transactionRepository;
    
    public Mono<ServerResponse> get(ServerRequest req) {
        return Mono.just(req.pathVariable("id"))
        .map(Integer::parseInt)
        .flatMap(transactionRepository::findById)
        .map(this::convertFromEntity)
        .flatMap(transaction -> ServerResponse.ok().bodyValue(transaction))
        .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> list(ServerRequest req) {
        List<String> tags = req.queryParams().get("tag");
        if (tags == null || tags.size() == 0) {
            return this.transactionRepository
            .findAllByUserID("testUser")
            .map(this::convertFromEntity)
            .collectList()
            .flatMap(transactions -> ServerResponse.ok().bodyValue(transactions));
        }

        return this.transactionRepository
        .findAllByUserID("testUser")
        .map(this::convertFromEntity)
        .collectList()
        .flatMap(transactions -> ServerResponse.ok().bodyValue(transactions));
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(TransactionRequest.class)
        .single()
        .map(this::validate)
        .map(this::convertToEntity)
        .flatMap(this.transactionRepository::save)
        .map(this::convertFromEntity)
        .flatMap(transaction -> ServerResponse.accepted().contentType(MediaType.APPLICATION_JSON).bodyValue(transaction))
        .onErrorResume(throwable -> {
            if (throwable instanceof NoSuchElementException) {
                return ServerResponse.badRequest().build();
            } else if (throwable instanceof ServerWebInputException) {
                ErrorResponse err = new ErrorResponse();
                err.setErrorMessage(throwable.getMessage());
                return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(err);
            }
            throwable.printStackTrace(); // TODO: log this with a logger
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }

    private TransactionRequest validate(TransactionRequest transaction) {
        Errors errors = new BeanPropertyBindingResult(transaction, "transaction");
        this.validator.validate(transaction, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.getAllErrors().toString());
        }
        return transaction;
    }

    private TransactionResponse convertFromEntity(TransactionEntity transactionEntity) {
        TransactionResponse t = new TransactionResponse();
        t.setAmount(transactionEntity.getAmount());
        t.setName(transactionEntity.getName());
        t.setVersion(transactionEntity.getVersion());
        t.setId(transactionEntity.getId());
        return t;
    }

    private TransactionEntity convertToEntity(TransactionRequest transaction, Integer id) {
        TransactionEntity e = new TransactionEntity();
        e.setId(id);
        e.setAmount(transaction.getAmount());
        e.setName(transaction.getName());
        e.setUserID("testUser");
        e.setVersion(transaction.getVersion());
        return e;
    }

    private TransactionEntity convertToEntity(TransactionRequest transaction) {
        return this.convertToEntity(transaction, null);
    }
}
