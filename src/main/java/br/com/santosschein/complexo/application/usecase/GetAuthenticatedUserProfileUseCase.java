package br.com.santosschein.complexo.application.usecase;

import br.com.santosschein.complexo.domain.model.User;
import br.com.santosschein.complexo.domain.repository.UserRepository;
import br.com.santosschein.complexo.interfaces.dto.response.UserProfileResponse;
import br.com.santosschein.complexo.shared.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class GetAuthenticatedUserProfileUseCase {

    private final UserRepository userRepository;

    public GetAuthenticatedUserProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse execute(Long authenticatedUserId) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(InvalidCredentialsException::new);

        return new UserProfileResponse(
                String.valueOf(user.id()),
                user.firstName(),
                user.lastName(),
                user.birthDate(),
                user.document(),
                user.email()
        );
    }
}
