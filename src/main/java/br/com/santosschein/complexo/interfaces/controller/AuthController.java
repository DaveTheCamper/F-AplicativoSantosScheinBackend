package br.com.santosschein.complexo.interfaces.controller;

import br.com.santosschein.complexo.application.usecase.auth.LoginUseCase;
import br.com.santosschein.complexo.application.usecase.auth.RegisterUserUseCase;
import br.com.santosschein.complexo.interfaces.dto.request.LoginRequest;
import br.com.santosschein.complexo.interfaces.dto.request.RegisterUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String token = loginUseCase.execute(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserRequest request) {
        registerUserUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
