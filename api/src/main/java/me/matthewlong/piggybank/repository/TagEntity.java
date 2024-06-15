package me.matthewlong.piggybank.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class TagEntity {
  @Getter @Setter @Id private Integer id;
  @Getter @Setter private String name;
}
