package me.matthewlong.piggybank.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class TransactionRequest {
  @Getter @Setter @NotNull private Integer amount;
  @Getter @Setter private List<String> tags;

  @Getter
  @Setter
  @NotEmpty(message = "name must not be empty")
  private String name;

  @Getter @Setter private String version;
}
