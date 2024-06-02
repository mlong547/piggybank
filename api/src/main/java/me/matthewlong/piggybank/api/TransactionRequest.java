package me.matthewlong.piggybank.api;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class TransactionRequest {
    @Getter @Setter @NotNull private Integer amount;
    @Getter @Setter private List<String> tags;

    @Getter
    @Setter
    @NotEmpty(message = "name must not be empty")
    private String name;

    @Getter @Setter private String version;
}
