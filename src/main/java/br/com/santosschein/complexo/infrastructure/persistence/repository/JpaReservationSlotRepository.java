package br.com.santosschein.complexo.infrastructure.persistence.repository;

import br.com.santosschein.complexo.infrastructure.persistence.entity.ReservationSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JpaReservationSlotRepository extends JpaRepository<ReservationSlotEntity, Long> {

    @Query(
            value = """
                    SELECT available_days.day_value
                    FROM (
                        SELECT TRUNC(:monthStart) + LEVEL - 1 AS day_value
                        FROM dual
                        CONNECT BY LEVEL <= TRUNC(:monthEnd) - TRUNC(:monthStart) + 1
                    ) available_days
                    WHERE EXISTS (
                        SELECT 1
                        FROM reservation_slots rs
                        JOIN soccer_fields sf ON sf.id = rs.field_id
                        WHERE rs.active = 1
                          AND sf.active = 1
                          AND NOT EXISTS (
                              SELECT 1
                              FROM reservations r
                              WHERE r.field_id = rs.field_id
                                AND r.slot_id = rs.id
                                AND TRUNC(r.reservation_date) = available_days.day_value
                                AND r.status = 'ACTIVE'
                          )
                    )
                    ORDER BY available_days.day_value
                    """,
            nativeQuery = true
    )
    List<LocalDate> findAvailableDaysBetween(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd
    );

    @Query(
            value = """
                    SELECT rs.*
                    FROM reservation_slots rs
                    JOIN soccer_fields sf ON sf.id = rs.field_id
                    WHERE rs.id IN (
                        SELECT MIN(available_rs.id)
                        FROM reservation_slots available_rs
                        JOIN soccer_fields available_sf ON available_sf.id = available_rs.field_id
                        WHERE available_rs.active = 1
                          AND available_sf.active = 1
                          AND NOT EXISTS (
                              SELECT 1
                              FROM reservations r
                              WHERE r.field_id = available_rs.field_id
                                AND r.slot_id = available_rs.id
                                AND TRUNC(r.reservation_date) = TRUNC(:reservationDate)
                                AND r.status = 'ACTIVE'
                          )
                        GROUP BY available_rs.start_time, available_rs.end_time, available_rs.price
                      )
                    ORDER BY rs.start_time
                    """,
            nativeQuery = true
    )
    List<ReservationSlotEntity> findAvailableSlotsByDate(@Param("reservationDate") LocalDate reservationDate);
}
