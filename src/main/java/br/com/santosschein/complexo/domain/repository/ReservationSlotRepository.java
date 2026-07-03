package br.com.santosschein.complexo.domain.repository;

import br.com.santosschein.complexo.domain.model.ReservationSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationSlotRepository {

    Optional<ReservationSlot> findById(Long id);

    List<ReservationSlot> findAvailableSlotsByDate(LocalDate reservationDate);
}
