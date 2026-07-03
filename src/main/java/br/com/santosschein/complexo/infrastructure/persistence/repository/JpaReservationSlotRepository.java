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
                    SELECT rs.*
                    FROM reservation_slots rs
                    WHERE rs.active = 1
                      AND NOT EXISTS (
                          SELECT 1
                          FROM reservations r
                          WHERE r.field_id = rs.field_id
                            AND r.slot_id = rs.id
                            AND TRUNC(r.reservation_date) = TRUNC(:reservationDate)
                            AND r.status = 'ACTIVE'
                      )
                    ORDER BY rs.field_id, rs.start_time
                    """,
            nativeQuery = true
    )
    List<ReservationSlotEntity> findAvailableSlotsByDate(@Param("reservationDate") LocalDate reservationDate);
}
