package com.pitang.desafiopitangapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider userAuthenticationProvider;

    public JwtAuthFilter(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/users",
            "/api/signin"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (PUBLIC_ENDPOINTS.contains(path) && "POST".equalsIgnoreCase(method)) {
            // Não valida token nem acessa SecurityContext, apenas segue o fluxo
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Remove "Bearer "
            try {
                var context = SecurityContextHolder.createEmptyContext();
                SecurityContextHolder.setContext(context);
                if ("GET".equalsIgnoreCase(method)) {
                    context.setAuthentication(userAuthenticationProvider.validateToken(token));
                } else {
                    context.setAuthentication(userAuthenticationProvider.validateTokenStrongly(token));
                }
            } catch (RuntimeException e) {
                // Qualquer falha na validação deve deixar o contexto limpo
                SecurityContextHolder.clearContext();
                // Deixa o Spring tratar como 401
            }
        }

        filterChain.doFilter(request, response);
    }
}
