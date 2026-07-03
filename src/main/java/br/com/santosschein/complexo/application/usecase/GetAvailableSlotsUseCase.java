package br.com.santosschein.complexo.application.usecase;

import br.com.santosschein.complexo.domain.model.ReservationSlot;
import br.com.santosschein.complexo.domain.repository.ReservationSlotRepository;
import br.com.santosschein.complexo.interfaces.dto.response.AvailableSlotResponse;
import br.com.santosschein.complexo.interfaces.dto.response.AvailableSlotsResponse;
import br.com.santosschein.complexo.shared.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetAvailableSlotsUseCase {

    private final ReservationSlotRepository reservationSlotRepository;

    public GetAvailableSlotsUseCase(ReservationSlotRepository reservationSlotRepository) {
        this.reservationSlotRepository = reservationSlotRepository;
    }

    public AvailableSlotsResponse execute(LocalDate date) {
        if (date == null) {
            throw new BusinessException("Data invalida.");
        }

        List<AvailableSlotResponse> slots = reservationSlotRepository.findAvailableSlotsByDate(date)
                .stream()
                .map(this::toResponse)
                .toList();

        return new AvailableSlotsResponse(slots);
    }

    private AvailableSlotResponse toResponse(ReservationSlot slot) {
        return new AvailableSlotResponse(
                "slot_" + slot.id(),
                slot.startTime(),
                slot.endTime(),
                slot.price()
        );
    }
}
