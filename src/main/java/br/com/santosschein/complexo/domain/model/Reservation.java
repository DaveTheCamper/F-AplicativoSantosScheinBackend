package br.com.santosschein.complexo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record Reservation(
        Long id,
        User user,
        SoccerField field,
        ReservationSlot slot,
        LocalDate reservationDate,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime canceledAt
) {
}
