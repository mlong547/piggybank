package me.matthewlong.piggybank.repository;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

public class TransactionEntity {
    @Getter @Setter @Id private Integer id;
    @Getter @Setter private Integer amount;
    @Getter @Setter private String name;
    @Getter @Setter private String version;
    @Getter @Setter private String userID;
}
