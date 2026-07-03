package br.com.santosschein.complexo.interfaces.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterUserRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        LocalDate birthDate,

        @NotBlank
        String document,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password
) {
}
