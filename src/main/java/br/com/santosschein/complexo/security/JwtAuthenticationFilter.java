package br.com.santosschein.complexo.security;

import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.shared.exception.ApiErrorResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final ApiErrorResponseWriter apiErrorResponseWriter;

    public JwtAuthenticationFilter(
            TokenService tokenService,
            UserDetailsService userDetailsService,
            ApiErrorResponseWriter apiErrorResponseWriter
    ) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.apiErrorResponseWriter = apiErrorResponseWriter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            if (tokenService.isValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateRequest(request, token);
            }
        } catch (RuntimeException exception) {
            apiErrorResponseWriter.write(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    "Sessao expirada ou token invalido.",
                    request.getRequestURI()
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(HttpServletRequest request, String token) {
        String email = tokenService.extractEmail(token);
        Long userId = tokenService.extractUserId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!(userDetails instanceof AuthenticatedUser authenticatedUser) || !authenticatedUser.id().equals(userId)) {
            throw new IllegalArgumentException("Token invalido para o usuario informado.");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
