package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.usecase.GetAvailableDaysUseCase;
import br.com.santosschein.complexo.application.usecase.GetAvailableSlotsUseCase;
import br.com.santosschein.complexo.interfaces.dto.response.AvailableDaysResponse;
import br.com.santosschein.complexo.interfaces.dto.response.AvailableSlotsResponse;
import br.com.santosschein.complexo.security.AuthenticatedUser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservations")
public class ReservationAvailabilityController {

    private final GetAvailableDaysUseCase getAvailableDaysUseCase;
    private final GetAvailableSlotsUseCase getAvailableSlotsUseCase;

    public ReservationAvailabilityController(
            GetAvailableDaysUseCase getAvailableDaysUseCase,
            GetAvailableSlotsUseCase getAvailableSlotsUseCase
    ) {
        this.getAvailableDaysUseCase = getAvailableDaysUseCase;
        this.getAvailableSlotsUseCase = getAvailableSlotsUseCase;
    }

    @GetMapping("/available-days")
    public AvailableDaysResponse availableDays(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        authenticatedUser.id();
        return getAvailableDaysUseCase.execute(year, month);
    }

    @GetMapping("/available-slots")
    public AvailableSlotsResponse availableSlots(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        authenticatedUser.id();
        return getAvailableSlotsUseCase.execute(date);
    }
}
