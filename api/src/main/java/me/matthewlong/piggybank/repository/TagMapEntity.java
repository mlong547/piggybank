package me.matthewlong.piggybank.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class TagMapEntity {
  @Getter @Setter @Id private Integer id;
  @Getter @Setter private Integer transactionId;
  @Getter @Setter private Integer tagId;
}
