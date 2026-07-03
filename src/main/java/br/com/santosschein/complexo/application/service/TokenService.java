package br.com.santosschein.complexo.application.service;

public interface TokenService {

    String generateAccessToken(Long userId, String email);

    boolean isValid(String token);

    Long extractUserId(String token);

    String extractEmail(String token);
}
