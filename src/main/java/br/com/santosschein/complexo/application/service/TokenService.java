package br.com.santosschein.complexo.application.service;

public interface TokenService {

    String generateAccessToken(Long userId, String email);
}
