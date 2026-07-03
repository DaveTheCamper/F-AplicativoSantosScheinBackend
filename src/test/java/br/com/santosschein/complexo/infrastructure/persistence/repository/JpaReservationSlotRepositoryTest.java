package br.com.santosschein.complexo.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class JpaReservationSlotRepositoryTest {

    @Test
    void availabilityQueriesOnlyBlockActiveReservations() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/br/com/santosschein/complexo/infrastructure/persistence/repository/JpaReservationSlotRepository.java"
        ));

        assertThat(source).contains("r.status = 'ACTIVE'");
        assertThat(source).doesNotContain("r.status <> 'CANCELED'");
    }
}
