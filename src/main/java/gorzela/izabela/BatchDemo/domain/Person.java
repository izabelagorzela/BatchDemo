package gorzela.izabela.BatchDemo.domain;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Person (Integer id, String name, LocalDate creationDate) {
}