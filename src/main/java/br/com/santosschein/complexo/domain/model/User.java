package br.com.santosschein.complexo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String document,
        String email,
        String passwordHash,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
