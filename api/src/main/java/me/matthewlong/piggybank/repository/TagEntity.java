package me.matthewlong.piggybank.repository;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

public class TagEntity {
    @Getter @Setter @Id private Integer id;
    @Getter @Setter private String name;
}
