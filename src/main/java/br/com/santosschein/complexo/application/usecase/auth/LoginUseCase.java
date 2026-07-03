package br.com.santosschein.complexo.application.usecase.auth;

import br.com.santosschein.complexo.application.service.PasswordService;
import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.domain.model.User;
import br.com.santosschein.complexo.domain.repository.UserRepository;
import br.com.santosschein.complexo.interfaces.dto.request.LoginRequest;
import br.com.santosschein.complexo.shared.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;

    public LoginUseCase(
            UserRepository userRepository,
            PasswordService passwordService,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    public String execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordService.matches(request.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return tokenService.generateAccessToken(user.id(), user.email());
    }
}
