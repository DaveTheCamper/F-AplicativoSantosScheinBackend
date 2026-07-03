package br.com.santosschein.complexo.infrastructure.persistence.repository;

import br.com.santosschein.complexo.domain.model.Reservation;
import br.com.santosschein.complexo.domain.model.ReservationStatus;
import br.com.santosschein.complexo.domain.repository.ReservationRepository;
import br.com.santosschein.complexo.infrastructure.persistence.entity.ReservationSlotEntity;
import br.com.santosschein.complexo.infrastructure.persistence.entity.SoccerFieldEntity;
import br.com.santosschein.complexo.infrastructure.persistence.entity.UserEntity;
import br.com.santosschein.complexo.infrastructure.persistence.mapper.ReservationPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final JpaReservationRepository jpaReservationRepository;
    private final EntityManager entityManager;
    private final ReservationPersistenceMapper mapper = new ReservationPersistenceMapper();

    public ReservationRepositoryAdapter(JpaReservationRepository jpaReservationRepository, EntityManager entityManager) {
        this.jpaReservationRepository = jpaReservationRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Reservation> findNextActiveReservation(Long userId, LocalDate fromDate) {
        return jpaReservationRepository.findNextActiveReservation(userId, fromDate).map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findFutureReservationsByUser(Long userId, LocalDate fromDate) {
        return jpaReservationRepository.findFutureReservationsByUser(userId, fromDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findPastReservationsByUser(Long userId, LocalDate beforeDate) {
        return jpaReservationRepository.findPastReservationsByUser(userId, beforeDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<LocalDate> findAvailableDaysBetween(LocalDate monthStart, LocalDate monthEnd) {
        return jpaReservationRepository.findAvailableDaysBetween(monthStart, monthEnd);
    }

    @Override
    public boolean existsByFieldSlotDateAndStatus(
            Long fieldId,
            Long slotId,
            LocalDate reservationDate,
            ReservationStatus status
    ) {
        return jpaReservationRepository.existsByField_IdAndSlot_IdAndReservationDateAndStatus(
                fieldId,
                slotId,
                reservationDate,
                status
        );
    }

    @Override
    public Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId) {
        return jpaReservationRepository.findByIdAndUser_Id(reservationId, userId).map(mapper::toDomain);
    }

    @Override
    public Reservation save(Reservation reservation) {
        UserEntity user = entityManager.getReference(UserEntity.class, reservation.user().id());
        SoccerFieldEntity field = entityManager.getReference(SoccerFieldEntity.class, reservation.field().id());
        ReservationSlotEntity slot = entityManager.getReference(ReservationSlotEntity.class, reservation.slot().id());

        return mapper.toDomain(jpaReservationRepository.saveAndFlush(mapper.toEntity(reservation, user, field, slot)));
    }
}
