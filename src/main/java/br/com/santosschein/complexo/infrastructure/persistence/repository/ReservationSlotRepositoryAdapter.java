package br.com.santosschein.complexo.infrastructure.persistence.repository;

import br.com.santosschein.complexo.domain.model.ReservationSlot;
import br.com.santosschein.complexo.domain.repository.ReservationSlotRepository;
import br.com.santosschein.complexo.infrastructure.persistence.mapper.ReservationSlotPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationSlotRepositoryAdapter implements ReservationSlotRepository {

    private final JpaReservationSlotRepository jpaReservationSlotRepository;
    private final ReservationSlotPersistenceMapper mapper = new ReservationSlotPersistenceMapper();

    public ReservationSlotRepositoryAdapter(JpaReservationSlotRepository jpaReservationSlotRepository) {
        this.jpaReservationSlotRepository = jpaReservationSlotRepository;
    }

    @Override
    public Optional<ReservationSlot> findById(Long id) {
        return jpaReservationSlotRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<LocalDate> findAvailableDaysBetween(LocalDate monthStart, LocalDate monthEnd) {
        return jpaReservationSlotRepository.findAvailableDaysBetween(monthStart, monthEnd);
    }

    @Override
    public List<ReservationSlot> findAvailableSlotsByDate(LocalDate reservationDate) {
        return jpaReservationSlotRepository.findAvailableSlotsByDate(reservationDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ReservationSlot> findFirstAvailableEquivalentSlot(Long slotId, LocalDate reservationDate) {
        return jpaReservationSlotRepository.findFirstAvailableEquivalentSlot(slotId, reservationDate)
                .map(mapper::toDomain);
    }
}
