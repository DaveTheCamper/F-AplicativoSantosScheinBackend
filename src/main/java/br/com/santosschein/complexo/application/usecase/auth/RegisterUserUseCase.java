package br.com.santosschein.complexo.application.usecase.auth;

import br.com.santosschein.complexo.application.service.PasswordService;
import br.com.santosschein.complexo.domain.model.User;
import br.com.santosschein.complexo.domain.repository.UserRepository;
import br.com.santosschein.complexo.interfaces.dto.request.RegisterUserRequest;
import br.com.santosschein.complexo.shared.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public void execute(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("E-mail ja cadastrado.");
        }

        if (userRepository.existsByDocument(request.document())) {
            throw new UserAlreadyExistsException("Documento ja cadastrado.");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                null,
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.document(),
                request.email(),
                passwordService.encode(request.password()),
                now,
                now
        );

        userRepository.save(user);
    }
}
