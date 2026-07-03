package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.usecase.GetAuthenticatedUserProfileUseCase;
import br.com.santosschein.complexo.interfaces.dto.response.UserProfileResponse;
import br.com.santosschein.complexo.security.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {

    private final GetAuthenticatedUserProfileUseCase getAuthenticatedUserProfileUseCase;

    public MeController(GetAuthenticatedUserProfileUseCase getAuthenticatedUserProfileUseCase) {
        this.getAuthenticatedUserProfileUseCase = getAuthenticatedUserProfileUseCase;
    }

    @GetMapping("/profile")
    public UserProfileResponse profile(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return getAuthenticatedUserProfileUseCase.execute(authenticatedUser.id());
    }
}
