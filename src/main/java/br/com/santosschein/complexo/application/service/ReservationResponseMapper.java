package br.com.santosschein.complexo.application.service;

import br.com.santosschein.complexo.domain.model.Reservation;
import br.com.santosschein.complexo.interfaces.dto.response.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationResponseMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                "res_" + reservation.id(),
                reservation.field().name(),
                reservation.reservationDate(),
                reservation.slot().startTime(),
                reservation.slot().endTime(),
                reservation.slot().price(),
                reservation.status()
        );
    }
}
