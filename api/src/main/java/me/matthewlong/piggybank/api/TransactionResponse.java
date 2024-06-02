package me.matthewlong.piggybank.api;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class TransactionResponse {
    @Getter @Setter private Integer amount;
    @Getter @Setter private List<String> tags;
    @Getter @Setter private String name;
    @Getter @Setter private String version;
    @Getter @Setter private Integer id;
}
