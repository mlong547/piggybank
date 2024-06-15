package me.matthewlong.piggybank.repository;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;

public class TransactionEntity {
  @Getter @Setter @Id private Integer id;
  @Getter @Setter private Integer amount;
  @Getter @Setter private String name;
  @Version @Getter @Setter private String version;
  @Getter @Setter private String userID;
  @Transient @Getter @Setter private List<String> tags;
}
