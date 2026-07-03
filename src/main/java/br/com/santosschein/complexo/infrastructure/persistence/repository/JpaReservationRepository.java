package br.com.santosschein.complexo.infrastructure.persistence.repository;

import br.com.santosschein.complexo.domain.model.ReservationStatus;
import br.com.santosschein.complexo.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query(
            value = """
                    SELECT r.*
                    FROM reservations r
                    JOIN reservation_slots rs ON rs.id = r.slot_id
                    WHERE r.user_id = :userId
                      AND r.status = 'ACTIVE'
                      AND TRUNC(r.reservation_date) >= TRUNC(:fromDate)
                    ORDER BY r.reservation_date ASC, rs.start_time ASC
                    FETCH FIRST 1 ROWS ONLY
                    """,
            nativeQuery = true
    )
    Optional<ReservationEntity> findNextActiveReservation(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDate fromDate
    );

    @Query("""
            SELECT r
            FROM ReservationEntity r
            WHERE r.user.id = :userId
              AND r.status = br.com.santosschein.complexo.domain.model.ReservationStatus.ACTIVE
              AND r.reservationDate >= :fromDate
            ORDER BY r.reservationDate ASC, r.slot.startTime ASC
            """)
    List<ReservationEntity> findFutureReservationsByUser(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDate fromDate
    );

    @Query("""
            SELECT r
            FROM ReservationEntity r
            WHERE r.user.id = :userId
              AND r.reservationDate < :beforeDate
            ORDER BY r.reservationDate DESC, r.slot.startTime DESC
            """)
    List<ReservationEntity> findPastReservationsByUser(
            @Param("userId") Long userId,
            @Param("beforeDate") LocalDate beforeDate
    );

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
                        WHERE rs.active = 1
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

    boolean existsByField_IdAndSlot_IdAndReservationDateAndStatus(
            Long fieldId,
            Long slotId,
            LocalDate reservationDate,
            ReservationStatus status
    );

    Optional<ReservationEntity> findByIdAndUser_Id(Long reservationId, Long userId);
}
