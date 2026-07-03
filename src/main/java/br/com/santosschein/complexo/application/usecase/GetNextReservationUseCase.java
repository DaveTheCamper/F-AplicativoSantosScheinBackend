package br.com.santosschein.complexo.application.usecase;

import br.com.santosschein.complexo.application.service.ReservationResponseMapper;
import br.com.santosschein.complexo.domain.repository.ReservationRepository;
import br.com.santosschein.complexo.interfaces.dto.response.ReservationResponse;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class GetNextReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationResponseMapper reservationResponseMapper;
    private final Clock clock;

    public GetNextReservationUseCase(
            ReservationRepository reservationRepository,
            ReservationResponseMapper reservationResponseMapper,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationResponseMapper = reservationResponseMapper;
        this.clock = clock;
    }

    public Optional<ReservationResponse> execute(Long authenticatedUserId) {
        return reservationRepository.findNextActiveReservation(
                        authenticatedUserId,
                        LocalDate.now(clock),
                        LocalTime.now(clock)
                )
                .map(reservationResponseMapper::toResponse);
    }
}
