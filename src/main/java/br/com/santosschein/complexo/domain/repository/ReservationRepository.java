package br.com.santosschein.complexo.domain.repository;

import br.com.santosschein.complexo.domain.model.Reservation;
import br.com.santosschein.complexo.domain.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findNextActiveReservation(Long userId, LocalDate fromDate);

    List<Reservation> findFutureReservationsByUser(Long userId, LocalDate fromDate);

    List<Reservation> findPastReservationsByUser(Long userId, LocalDate beforeDate);

    List<LocalDate> findAvailableDaysBetween(LocalDate monthStart, LocalDate monthEnd);

    boolean existsByFieldSlotDateAndStatus(
            Long fieldId,
            Long slotId,
            LocalDate reservationDate,
            ReservationStatus status
    );

    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);

    Reservation save(Reservation reservation);
}
