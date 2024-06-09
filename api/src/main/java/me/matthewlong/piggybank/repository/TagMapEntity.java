package me.matthewlong.piggybank.repository;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

public class TagMapEntity {
    @Getter @Setter @Id private Integer id;
    @Getter @Setter private Integer transactionId;
    @Getter @Setter private Integer tagId;
}
