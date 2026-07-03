package br.com.santosschein.complexo.application.usecase.auth;

import br.com.santosschein.complexo.application.service.PasswordService;
import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.domain.model.User;
import br.com.santosschein.complexo.domain.repository.UserRepository;
import br.com.santosschein.complexo.interfaces.dto.request.LoginRequest;
import br.com.santosschein.complexo.shared.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginUseCase useCase;

    @Test
    void logsInSuccessfully() {
        LoginRequest request = new LoginRequest("joao@email.com", "Senha@123");
        User user = user();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordService.matches(request.password(), user.passwordHash())).thenReturn(true);
        when(tokenService.generateAccessToken(user.id(), user.email())).thenReturn("jwt-token");

        String token = useCase.execute(request);

        assertThat(token).isEqualTo("jwt-token");
        verify(tokenService).generateAccessToken(user.id(), user.email());
    }

    @Test
    void rejectsLoginWithInvalidPassword() {
        LoginRequest request = new LoginRequest("joao@email.com", "senha-errada");
        User user = user();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordService.matches(request.password(), user.passwordHash())).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenService, never()).generateAccessToken(user.id(), user.email());
    }

    @Test
    void rejectsLoginWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest("naoexiste@email.com", "Senha@123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(passwordService, never()).matches(request.password(), "$2a$hash");
        verify(tokenService, never()).generateAccessToken(1L, request.email());
    }

    private User user() {
        LocalDateTime now = LocalDateTime.now();
        return new User(
                1L,
                "Joao",
                "Silva",
                LocalDate.of(1995, 4, 20),
                "12345678900",
                "joao@email.com",
                "$2a$hash",
                now,
                now
        );
    }
}
